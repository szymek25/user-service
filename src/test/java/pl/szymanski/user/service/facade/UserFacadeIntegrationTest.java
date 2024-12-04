package pl.szymanski.user.service.facade;

import io.swagger.client.ApiException;
import io.swagger.client.model.CredentialRepresentation;
import io.swagger.client.model.UserRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.exception.DuplicatedUserException;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.matcher.UserRepresentationMatcher;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserFacadeIntegrationTest {

	@Resource(name = "userFacadeWithKeycloakUserCalls")
	private UserFacade userFacade;

	@MockBean(name = "keycloakUserServiceForApiCalls")
	private KeycloakUserService keycloakUserService;

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldSendCreateRequest() throws ApiException {
		// given
		AddUserDTO addUserDTO = new AddUserDTO();
		addUserDTO.setName("firstName");
		addUserDTO.setLastName("lastName");
		addUserDTO.setEmail("test@test.com");
		addUserDTO.setPhone("111111");
		addUserDTO.setAddressLine1("test address");
		addUserDTO.setRoleId("13fbb108-1b6e-46f5-9e0d-d78f4bca1efc");
		addUserDTO.setPostalCode("1111");
		addUserDTO.setTown("test Town");
		addUserDTO.setDayOfBirth("1990-01-01");
		addUserDTO.setPassword("examplePassword");

		userFacade.addUser(addUserDTO);

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setEmail("test@test.com");
		userRepresentation.setUsername("test@test.com");
		userRepresentation.setGroups(List.of("/ROLE_EMPLOYEE"));
		userRepresentation.setFirstName("firstName");
		userRepresentation.setLastName("lastName");
		HashMap<String, List<String>> attributes = new HashMap<>();
		attributes.put("phone", List.of("111111"));
		attributes.put("addressLine1", List.of("test address"));
		attributes.put("postalCode", List.of("1111"));
		attributes.put("town", List.of("test Town"));
		attributes.put("dayOfBirth", List.of("1990-01-01"));
		userRepresentation.enabled(true);
		userRepresentation.emailVerified(true);
		final CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(ApplicationConstants.KeyCloak.CREDENTIAL_TYPE_PASSWORD);
		credential.setValue("examplePassword");
		userRepresentation.setCredentials(Collections.singletonList(credential));

		userRepresentation.setAttributes(attributes);
		verify(keycloakUserService).createUser(argThat(new UserRepresentationMatcher(userRepresentation)));
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldThrowDuplicatedUserException() throws ApiException {
		AddUserDTO addUserDTO = new AddUserDTO();
		addUserDTO.setEmail("admin@biblioteka.com");

		Assertions.assertThrows(DuplicatedUserException.class, () -> userFacade.addUser(addUserDTO));
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldSendRegistrationRequest() throws ApiException {
		// given
		RegisterUserDTO registerUserDTO = new RegisterUserDTO();
		registerUserDTO.setName("firstName");
		registerUserDTO.setLastName("lastName");
		registerUserDTO.setEmail("test@test.com");
		registerUserDTO.setPhone("111111");
		registerUserDTO.setAddressLine1("test address");
		registerUserDTO.setPostalCode("1111");
		registerUserDTO.setTown("test Town");
		registerUserDTO.setDayOfBirth("1990-01-01");
		registerUserDTO.setPassword("examplePassword");

		userFacade.registerUser(registerUserDTO);

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setEmail("test@test.com");
		userRepresentation.setUsername("test@test.com");
		userRepresentation.setGroups(List.of("/ROLE_USER"));
		userRepresentation.setFirstName("firstName");
		userRepresentation.setLastName("lastName");
		HashMap<String, List<String>> attributes = new HashMap<>();
		attributes.put("phone", List.of("111111"));
		attributes.put("addressLine1", List.of("test address"));
		attributes.put("postalCode", List.of("1111"));
		attributes.put("town", List.of("test Town"));
		attributes.put("dayOfBirth", List.of("1990-01-01"));
		userRepresentation.enabled(true);
		userRepresentation.emailVerified(true);
		final CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(ApplicationConstants.KeyCloak.CREDENTIAL_TYPE_PASSWORD);
		credential.setValue("examplePassword");
		userRepresentation.setCredentials(Collections.singletonList(credential));

		userRepresentation.setAttributes(attributes);
		verify(keycloakUserService).createUser(argThat(new UserRepresentationMatcher(userRepresentation)));
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldThrowDuplicatedUserDuringRegistrationException() throws ApiException {
		RegisterUserDTO registerUserDTO = new RegisterUserDTO();
		registerUserDTO.setEmail("admin@biblioteka.com");

		Assertions.assertThrows(DuplicatedUserException.class, () -> userFacade.registerUser(registerUserDTO));
	}
}
