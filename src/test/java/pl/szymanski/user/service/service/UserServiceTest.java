package pl.szymanski.user.service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanski.user.service.dao.UserDao;
import pl.szymanski.user.service.exception.UserNotFoundException;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.impl.UserServiceImpl;

import java.sql.Date;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	private static final String KEYCLOAK_ID = "ddd-fff";
	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserDao userDao;
	@Test
	public void shouldThrowUserNotFoundException() {
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(new User(),""));
	}

	@Test
	public void shouldUdpateUser() {
		Mockito.when(userDao.findByKeycloakId(KEYCLOAK_ID)).thenReturn(getMockExistingUser());

		User updatedUser = userService.update(getUpdatedUserEntry(), KEYCLOAK_ID);
		Assertions.assertEquals("updatedName", updatedUser.getName());
		Assertions.assertEquals("updatedName", updatedUser.getLastName());
		Assertions.assertEquals("updatedEmail", updatedUser.getEmail());
		Assertions.assertEquals("updatedPhone", updatedUser.getPhone());
		Assertions.assertEquals("updatedAddressLine1", updatedUser.getAddressLine1());
		Assertions.assertEquals(Date.valueOf("2000-01-01"), updatedUser.getDayOfBirth());
		Assertions.assertEquals("12345", updatedUser.getPostalCode());
		Assertions.assertEquals("updatedTown", updatedUser.getTown());
	}

	private User getMockExistingUser() {
		User user = new User();
		user.setKeycloakId(KEYCLOAK_ID);
		user.setName("name");
		user.setLastName("lastName");
		user.setEmail("email");
		user.setPhone("phone");
		user.setAddressLine1("addressLine1");
		user.setDayOfBirth(null);
		user.setPostalCode("postalCode");
		user.setTown("town");
		return user;
	}

	private User getUpdatedUserEntry() {
		User user = new User();
		user.setKeycloakId(KEYCLOAK_ID);
		user.setName("updatedName");
		user.setLastName("updatedName");
		user.setEmail("updatedEmail");
		user.setPhone("updatedPhone");
		user.setAddressLine1("updatedAddressLine1");
		user.setDayOfBirth(Date.valueOf("2000-01-01"));
		user.setPostalCode("12345");
		user.setTown("updatedTown");
		return user;
	}
}
