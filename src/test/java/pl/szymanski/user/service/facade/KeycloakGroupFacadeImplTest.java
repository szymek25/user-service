package pl.szymanski.user.service.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.impl.KeycloakGroupFacadeImpl;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class KeycloakGroupFacadeImplTest {

	private static final String SAMPLE_USER_ID = "442-3ddd-ffff";
	private static final String SAMPLE_GROUP_ID = "ddd-2wss-fff";

	@InjectMocks
	private KeycloakGroupFacadeImpl keycloakGroupFacadeImpl;

	@Mock
	private RoleService roleService;

	@Mock
	private UserService userService;

	@Test
	public void shouldAssignUserToGroup() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "GROUP_MEMBERSHIP");
		Role stubRole = stubRole(SAMPLE_GROUP_ID);
		Mockito.when(roleService.getById(SAMPLE_GROUP_ID)).thenReturn(stubRole);
		User stubUser = stubUser(SAMPLE_USER_ID);
		Mockito.when(userService.findByKeycloakId(SAMPLE_USER_ID)).thenReturn(stubUser);

		keycloakGroupFacadeImpl.processGroupUpdate(eventDTO);

		Mockito.verify(userService, Mockito.atLeastOnce()).assignRole(stubUser, stubRole);

	}

	@Test
	public void shouldRemoveUserFromGroup() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("DELETE", "GROUP_MEMBERSHIP");
		User stubUser = stubUser(SAMPLE_USER_ID);
		Mockito.when(userService.findByKeycloakId(SAMPLE_USER_ID)).thenReturn(stubUser);

		keycloakGroupFacadeImpl.processGroupUpdate(eventDTO);

		Mockito.verify(userService, Mockito.atLeastOnce()).assignRole(stubUser, null);
		Mockito.verify(roleService, Mockito.never()).getById(Mockito.any());
	}

	@Test
	public void shouldThrowExceptionIfResourceTypeIsNotUser() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "NOT_GROUP_MEMBERSHIP");
		Assertions.assertThrows(IllegalArgumentException.class, () -> keycloakGroupFacadeImpl.processGroupUpdate(eventDTO));
	}

	@Test
	public void shouldThrowExceptionIfOperationTypeIsNotValid() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("INVALID", "GROUP_MEMBERSHIP");
		Assertions.assertThrows(IllegalArgumentException.class, () -> keycloakGroupFacadeImpl.processGroupUpdate(eventDTO));
	}

	@Test
	public void shouldThrowExceptionIfResourcePathIsInvalid() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "GROUP_MEMBERSHIP");
		eventDTO.setResourcePath("dddd");
		Assertions.assertThrows(IllegalArgumentException.class, () -> keycloakGroupFacadeImpl.processGroupUpdate(eventDTO));
	}

	private KeycloakAdminEventDTO prepareKeycloakAdminEventDTO(String operationType, String resourceType) {
		KeycloakAdminEventDTO keycloakAdminEventDTO = new KeycloakAdminEventDTO();
		keycloakAdminEventDTO.setOperationType(operationType);
		keycloakAdminEventDTO.setResourceType(resourceType);
		keycloakAdminEventDTO.setResourcePath("users/" + KeycloakGroupFacadeImplTest.SAMPLE_USER_ID + "/groups/" + KeycloakGroupFacadeImplTest.SAMPLE_GROUP_ID);

		return keycloakAdminEventDTO;
	}

	private User stubUser(final String sampleUserId) {
		final User user = new User();
		user.setKeycloakId(sampleUserId);
		return user;
	}

	private Role stubRole(String id) {
		final Role role = new Role();
		role.setId(id);
		return role;
	}
}
