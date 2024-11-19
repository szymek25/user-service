package pl.szymanski.user.service.listener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import java.sql.Date;

@SpringBootTest
public class UpdateUserEventListenerIntegrationTest {

	@Autowired
	private UpdateUserEventListener updateUserEventListener;

	@Autowired
	private UserService userService;

	@MockBean
	private KeycloakUserService mockKeycloakUserService;

	@BeforeEach
	public void setup() {
		Mockito.when(mockKeycloakUserService.updateUser(Mockito.any())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldUpdateUser() {
		String keycloakId = "f1b1b1b1-1b1b-1b1b-1b1b-1b1b1b1b1b14";
		UpdateUserEvent updateUserEvent = createUpdateUserEvent(keycloakId);

		updateUserEventListener.handleUserUpdate(updateUserEvent);

		User updatedUser = userService.findByKeycloakId(keycloakId);
		Assertions.assertEquals("updatedName", updatedUser.getName());
		Assertions.assertEquals("updatedName", updatedUser.getLastName());
		Assertions.assertEquals("updatedEmail", updatedUser.getEmail());
		Assertions.assertEquals("updatedPhone", updatedUser.getPhone());
		Assertions.assertEquals("updatedAddressLine1", updatedUser.getAddressLine1());
		Assertions.assertEquals(Date.valueOf("2000-01-01"), updatedUser.getDayOfBirth());
		Assertions.assertEquals("12345", updatedUser.getPostalCode());
		Assertions.assertEquals("updatedTown", updatedUser.getTown());
	}

	private UpdateUserEvent createUpdateUserEvent(String keycloakId) {
		return UpdateUserEvent.newBuilder()
				.setId(keycloakId)
				.setName("updatedName")
				.setLastName("updatedName")
				.setEmail("updatedEmail")
				.setPhone("updatedPhone")
				.setAddressLine1("updatedAddressLine1")
				.setDayOfBirth("2000-01-01")
				.setPostalCode("12345")
				.setTown("updatedTown")
				.build();
	}
}
