package pl.szymanski.user.service.init;

import io.swagger.client.model.GroupRepresentation;
import io.swagger.client.model.UserRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanski.user.service.keycloak.api.KeycloakGroupService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.mapper.RoleGroupRepresentationMapper;
import pl.szymanski.user.service.mapper.UserMapper;
import pl.szymanski.user.service.model.InitState;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.service.InitStateService;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataBaseInitializerTest {

	public static final String USER_ID = "ee-e11-dd";
	public static final String GROUP_ID = "dd-22-ee";
	@InjectMocks
	private DataBaseInitializer dataBaseInitializer;
	@Mock
	private InitStateService initStateService;


	@Mock
	private UserMapper userMapper;

	@Mock
	private RoleGroupRepresentationMapper roleGroupRepresentationMapper;

	@Mock
	private KeycloakUserService keycloakUserService;

	@Mock
	private KeycloakGroupService keycloakGroupService;

	@Mock
	private UserService userService;

	@Mock
	private RoleService roleService;

	@Test
	public void testInitWhenDBAlreadyInitialized() {
		InitState initState = new InitState();
		initState.setInitialized(true);
		when(initStateService.getLatestInitState()).thenReturn(initState);

		dataBaseInitializer.initializeDBAfterStartup();
		verify(initStateService, times(1)).getLatestInitState();
		verify(keycloakUserService, never()).getUsers();
		verify(userService, never()).deleteAll();
		verify(userService, never()).saveAll(any());
		verify(roleService, never()).deleteAll();
		verify(keycloakGroupService, never()).getGroups();
		verify(roleService, never()).saveAll(any());
		verify(keycloakGroupService, never()).getMembersOfGroup(any());
		verify(userService, never()).findAllByKeycloakId(any());
	}

	@Test
	public void testInitWhenDBNotInitialized() {
		when(keycloakUserService.getUsers()).thenReturn(getSampleUserRepresentation());
		when(keycloakGroupService.getGroups()).thenReturn(getSampleGroup());
		when(keycloakGroupService.getMembersOfGroup(matches(GROUP_ID))).thenReturn(getSampleUserRepresentation());
		when(roleGroupRepresentationMapper.groupRepresentationsToRoles(any())).thenReturn(getSampleConvertedRoles());

		dataBaseInitializer.initializeDBAfterStartup();

		verify(initStateService, times(1)).getLatestInitState();
		verify(keycloakUserService, times(1)).getUsers();
		verify(userService, times(1)).deleteAll();
		verify(userService, times(2)).saveAll(any());
		verify(roleService, times(1)).deleteAll();
		verify(keycloakGroupService, times(1)).getGroups();
		verify(roleService, times(1)).saveAll(any());
		verify(keycloakGroupService, times(1)).getMembersOfGroup(any());
		ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(userService, times(1)).findAllByKeycloakId(argumentCaptor.capture());
		List<String> capturedValue = argumentCaptor.getValue();
		Assertions.assertTrue(capturedValue.contains(USER_ID));
	}

	private List<Role> getSampleConvertedRoles() {
		List<Role> results = new ArrayList<>();
		Role role = new Role();
		role.setId(GROUP_ID);
		results.add(role);
		return results;
	}

	private List<GroupRepresentation> getSampleGroup() {
		List<GroupRepresentation> results = new ArrayList<>();

		GroupRepresentation groupRepresentation = new GroupRepresentation();
		groupRepresentation.setId(GROUP_ID);
		results.add(groupRepresentation);

		return results;
	}

	private List<UserRepresentation> getSampleUserRepresentation() {
		List<UserRepresentation> results = new ArrayList<>();
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName("John");
		userRepresentation.setLastName("Doe");
		userRepresentation.setEmail("johnDoe@test.com");
		userRepresentation.setId(USER_ID);
		results.add(userRepresentation);

		return results;
	}
}
