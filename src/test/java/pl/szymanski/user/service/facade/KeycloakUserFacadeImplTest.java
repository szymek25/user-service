package pl.szymanski.user.service.facade;

import io.swagger.client.model.UserRepresentation;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.impl.KeycloakUserFacadeImpl;
import pl.szymanski.user.service.mapper.UserMapper;
import pl.szymanski.user.service.matcher.UserMatcher;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeycloakUserFacadeImplTest {

	private static final String SAMPLE_ID = "eee-f11-ff";
	public static final User STUB_USER_FROM_UPDATE_EVENT = createStubUser("", "updated@test.com", "Updated name", "Updated lastname", "987654321", "Updated line", "Updated town", "54321", "1995-03-01");

	private static final User STUB_USER_IN_DB = createStubUser(SAMPLE_ID, "test@test.com", "John", "Doe", "123456789", "Test", "Test", "12345", "1990-01-01");
	@InjectMocks
	private KeycloakUserFacadeImpl keycloakUserFacadeImpl;

	@Mock
	private UserService userService;

	@Mock
	private UserMapper userMapper;


	@Test
	public void shouldCreateNewUserIfNotFoundInDBAndOperationTypeIsCreate() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "USER", SAMPLE_ID);
		when(userMapper.map(any(UserRepresentation.class))).thenReturn(STUB_USER_FROM_UPDATE_EVENT);
		keycloakUserFacadeImpl.processUserUpdate(eventDTO);

		Mockito.verify(userService).save(argThat(new UserMatcher(STUB_USER_FROM_UPDATE_EVENT)));
	}

	@Test
	public void shouldCreateNewUserIfNotFoundInDBAndOperationIsUpdate() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("UPDATE", "USER", SAMPLE_ID);
		when(userMapper.map(any(UserRepresentation.class))).thenReturn(STUB_USER_FROM_UPDATE_EVENT);


		keycloakUserFacadeImpl.processUserUpdate(eventDTO);

		verify(userService).save(argThat(new UserMatcher(STUB_USER_FROM_UPDATE_EVENT)));
	}

	@Test
	public void shouldUpdateUserIfFoundInDBAndOperationTypeIsUpdate() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("UPDATE", "USER", SAMPLE_ID);
		when(userService.findByKeycloakId(eq(SAMPLE_ID))).thenReturn(STUB_USER_IN_DB);
		when(userMapper.map(any(UserRepresentation.class))).thenReturn(STUB_USER_FROM_UPDATE_EVENT);

		keycloakUserFacadeImpl.processUserUpdate(eventDTO);

		verify(userService).save(argThat(new UserMatcher(STUB_USER_FROM_UPDATE_EVENT)));
	}

	@Test
	public void shouldUpdateUserIfFoundInDBAndOperationTypeIsCreate() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "USER", SAMPLE_ID);
		when(userService.findByKeycloakId(eq(SAMPLE_ID))).thenReturn(STUB_USER_IN_DB);
		when(userMapper.map(any(UserRepresentation.class))).thenReturn(STUB_USER_FROM_UPDATE_EVENT);


		keycloakUserFacadeImpl.processUserUpdate(eventDTO);

		verify(userService).save(argThat(new UserMatcher(STUB_USER_FROM_UPDATE_EVENT)));
	}

	@Test
	public void shouldRemoveUserWhenOperationTypeIsDelete() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("DELETE", "USER", SAMPLE_ID);
		when(userMapper.map(any(UserRepresentation.class))).thenReturn(STUB_USER_FROM_UPDATE_EVENT);

		keycloakUserFacadeImpl.processUserUpdate(eventDTO);

		verify(userService).delete(SAMPLE_ID);
	}

	@Test
	public void shouldThrowExceptionIfResourceTypeIsNotUser() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "NOT_USER", SAMPLE_ID);
		Assertions.assertThrows(IllegalArgumentException.class, () -> keycloakUserFacadeImpl.processUserUpdate(eventDTO));
	}

	@Test
	public void shouldThrowExceptionIfOperationTypeIsNotValid() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("INVALID", "USER", SAMPLE_ID);
		Assertions.assertThrows(IllegalArgumentException.class, () -> keycloakUserFacadeImpl.processUserUpdate(eventDTO));
	}

	private static User createStubUser(String keycloakId, String email, String firstName, String lastName, String phone, String addressLine1, String town, String postalCode, String dayOfBirth) {
		User user = new User();
		user.setKeycloakId(keycloakId);
		user.setEmail(email);
		user.setName(firstName);
		user.setLastName(lastName);
		user.setPhone(phone);
		user.setAddressLine1(addressLine1);
		user.setTown(town);
		user.setPostalCode(postalCode);
		user.setDayOfBirth(Date.valueOf(dayOfBirth));
		return user;
	}

	private KeycloakAdminEventDTO prepareKeycloakAdminEventDTO(String operationType, String resourceType, String keycloakId) {
		Map<String, String> map = new HashMap<>();

		KeycloakAdminEventDTO keycloakAdminEventDTO = new KeycloakAdminEventDTO();
		keycloakAdminEventDTO.setOperationType(operationType);
		keycloakAdminEventDTO.setResourceType(resourceType);
		keycloakAdminEventDTO.setRepresentation(new JSONObject(map).toString());
		keycloakAdminEventDTO.setResourcePath("users/" + keycloakId);

		return keycloakAdminEventDTO;
	}

}
