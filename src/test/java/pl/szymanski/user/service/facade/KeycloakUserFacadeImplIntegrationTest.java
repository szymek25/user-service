package pl.szymanski.user.service.facade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class KeycloakUserFacadeImplIntegrationTest {

	private static final String TEST_EMAIL = "test@test.com";
	private static final String TEST_NAME = "John";
	private static final String TEST_LAST_NAME = "Doe";
	private static final String TEST_PHONE = "123456789";
	private static final String TEST_ADDRESS_LINE = "Test";
	private static final String TEST_TOWN = "Test Town";
	private static final String TEST_POSTAL_CODE = "12345";
	private static final Date TEST_DATE = Date.valueOf("1990-01-01");

	@Autowired
	private KeycloakUserFacade keycloakUserFacade;

	@Autowired
	private UserService userService;

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldCreateUser() throws JSONException {
		final String sampleKeycloakId = "1ff-1ff-1ff";
		final KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE", "USER", sampleKeycloakId);

		keycloakUserFacade.processUserUpdate(eventDTO);

		final User byKeycloakId = userService.findByKeycloakId(sampleKeycloakId);
		assertEquals(TEST_EMAIL, byKeycloakId.getEmail());
		assertEquals(TEST_NAME, byKeycloakId.getName());
		assertEquals(TEST_LAST_NAME, byKeycloakId.getLastName());
		assertEquals(TEST_PHONE, byKeycloakId.getPhone());
		assertEquals(TEST_ADDRESS_LINE, byKeycloakId.getAddressLine1());
		assertEquals(TEST_TOWN, byKeycloakId.getTown());
		assertEquals(TEST_POSTAL_CODE, byKeycloakId.getPostalCode());
		assertEquals(TEST_DATE, byKeycloakId.getDayOfBirth());
		assertEquals(sampleKeycloakId, byKeycloakId.getKeycloakId());
		assertEquals(ApplicationConstants.ROLE_EMPLOYEE, byKeycloakId.getRole().getName());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldUpdateAlreadyExistingUser() throws JSONException {
		final String keycloakId = "aa183f01-9487-437e-9d40-6665286fd641";
		final KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("UPDATE", "USER", keycloakId);

		keycloakUserFacade.processUserUpdate(eventDTO);

		final User byKeycloakId = userService.findByKeycloakId(keycloakId);
		assertEquals(TEST_EMAIL, byKeycloakId.getEmail());
		assertEquals(TEST_NAME, byKeycloakId.getName());
		assertEquals(TEST_LAST_NAME, byKeycloakId.getLastName());
		assertEquals(TEST_PHONE, byKeycloakId.getPhone());
		assertEquals(TEST_ADDRESS_LINE, byKeycloakId.getAddressLine1());
		assertEquals(TEST_TOWN, byKeycloakId.getTown());
		assertEquals(TEST_POSTAL_CODE, byKeycloakId.getPostalCode());
		assertEquals(TEST_DATE, byKeycloakId.getDayOfBirth());
		assertEquals(keycloakId, byKeycloakId.getKeycloakId());
	}

	private KeycloakAdminEventDTO prepareKeycloakAdminEventDTO(final String operationType, final String resourceType, final String keycloakId) throws JSONException {
		final Map<String, String> map = new HashMap<>();
		map.put("email", TEST_EMAIL);
		map.put("firstName", TEST_NAME);
		map.put("lastName", TEST_LAST_NAME);
		final Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("phone", Collections.singletonList(TEST_PHONE));
		attributes.put("addressLine1", Collections.singletonList(TEST_ADDRESS_LINE));
		attributes.put("town", Collections.singletonList(TEST_TOWN));
		attributes.put("postalCode", Collections.singletonList(TEST_POSTAL_CODE));
		attributes.put("dayOfBirth", Collections.singletonList("1990-01-01"));
		JSONObject representationJson = new JSONObject(map);
		representationJson.put("attributes", new JSONObject(attributes));
		representationJson.put("groups", new JSONArray(List.of("/" + ApplicationConstants.ROLE_EMPLOYEE)));

		KeycloakAdminEventDTO keycloakAdminEventDTO = new KeycloakAdminEventDTO();
		keycloakAdminEventDTO.setOperationType(operationType);
		keycloakAdminEventDTO.setResourceType(resourceType);
		keycloakAdminEventDTO.setRepresentation(representationJson.toString());
		keycloakAdminEventDTO.setResourcePath("users/" + keycloakId);

		return keycloakAdminEventDTO;
	}
}
