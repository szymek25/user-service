package pl.szymanski.user.service.mapper;


import io.swagger.client.model.UserRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void testUserMapping() {
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName("John");
		userRepresentation.setLastName("Doe");
		userRepresentation.setEmail("john.doe@example.com");
		userRepresentation.setId("123456789");
		userRepresentation.setAttributes(Map.of(ApplicationConstants.KeyCloak.PHONE, List.of("123456789"), ApplicationConstants.KeyCloak.ADDRESS_LINE_1, List.of("Main St. 123"), ApplicationConstants.KeyCloak.TOWN, List.of("Springfield"), ApplicationConstants.KeyCloak.POSTAL_CODE, List.of("12345"),ApplicationConstants.KeyCloak.BIRTHDATE, List.of("1990-01-01")));
		userRepresentation.setGroups(List.of("/" + ApplicationConstants.USER_ROLE_NAME));

		User user = userMapper.map(userRepresentation);
		assertEquals("John", user.getName());
		assertEquals("Doe", user.getLastName());
		assertEquals("john.doe@example.com", user.getEmail());
		assertEquals("123456789", user.getPhone());
		assertEquals("Main St. 123", user.getAddressLine1());
		assertEquals("Springfield", user.getTown());
		assertEquals("12345", user.getPostalCode());
		assertEquals("1990-01-01", user.getDayOfBirth().toString());
		assertEquals("123456789", user.getKeycloakId());
		assertEquals(ApplicationConstants.USER_ROLE_NAME, user.getRole().getName());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void testUserRepresentationMapping() {
		String id = "aa183f01-9487-437e-9d40-6665286fd641";
		User user = userService.findByKeycloakId(id);
		UserRepresentation userRepresentation = userMapper.mapToUserRepresentation(user);
		assertEquals("admin", userRepresentation.getFirstName());
		assertEquals("admin", userRepresentation.getLastName());
		assertEquals(id, userRepresentation.getId());
		assertEquals("admin@biblioteka.com", userRepresentation.getEmail());
		assertEquals("admin@biblioteka.com", userRepresentation.getUsername());
		assertEquals(Map.of(ApplicationConstants.KeyCloak.PHONE, List.of("512155211"), ApplicationConstants.KeyCloak.ADDRESS_LINE_1, List.of("Biblioteczna"), ApplicationConstants.KeyCloak.TOWN, List.of("Zabrze"), ApplicationConstants.KeyCloak.POSTAL_CODE, List.of("41-800"),ApplicationConstants.KeyCloak.BIRTHDATE, List.of("1996-01-19")), userRepresentation.getAttributes());
		assertEquals(List.of("/" + ApplicationConstants.ROLE_EMPLOYEE_NAME), userRepresentation.getGroups());
	}

}
