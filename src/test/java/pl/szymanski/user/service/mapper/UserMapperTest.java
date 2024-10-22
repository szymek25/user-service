package pl.szymanski.user.service.mapper;


import io.swagger.client.model.UserRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.model.User;

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testUserMapping() {
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName("John");
		userRepresentation.setLastName("Doe");
		userRepresentation.setEmail("john.doe@example.com");
		userRepresentation.setId("123456789");
		userRepresentation.setAttributes(Map.of(ApplicationConstants.KeyCloak.PHONE, List.of("123456789"), ApplicationConstants.KeyCloak.ADDRESS_LINE_1, List.of("Main St. 123"), ApplicationConstants.KeyCloak.TOWN, List.of("Springfield"), ApplicationConstants.KeyCloak.POSTAL_CODE, List.of("12345"),ApplicationConstants.KeyCloak.BIRTHDATE, List.of("1990-01-01")));

		User user = userMapper.map(userRepresentation);
		Assertions.assertEquals("John", user.getName());
		Assertions.assertEquals("Doe", user.getLastName());
		Assertions.assertEquals("john.doe@example.com", user.getEmail());
		Assertions.assertEquals("123456789", user.getPhone());
		Assertions.assertEquals("Main St. 123", user.getAddressLine1());
		Assertions.assertEquals("Springfield", user.getTown());
		Assertions.assertEquals("12345", user.getPostalCode());
		Assertions.assertEquals("1990-01-01", user.getDayOfBirth().toString());
		Assertions.assertEquals("123456789", user.getKeycloakId());
	}


}
