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


	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldUpdateUserAndRole() {
		String keycloakId = "f1b1b1b1-1b1b-1b1b-1b1b-1b1b1b1b1b14";
		String keycloakRoleId= "13fbb108-1b6e-46f5-9e0d-d78f4bca1efc";
		UpdateUserEvent updateUserEvent = createUpdateUserEvent(keycloakId, keycloakRoleId);

		Mockito.when(mockKeycloakUserService.updateUser(Mockito.any())).thenReturn(true);
		Mockito.when(mockKeycloakUserService.assignRole(Mockito.any(), Mockito.any())).thenReturn(true);

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
		Assertions.assertEquals(keycloakRoleId, updatedUser.getRole().getId());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldUpdateOnlyUser() {
		String keycloakId = "f1b1b1b1-1b1b-1b1b-1b1b-1b1b1b1b1b14";
		String keycloakRoleId= "ca704dd5-067d-4847-a9b2-06c91c4b8744";
		UpdateUserEvent updateUserEvent = createUpdateUserEvent(keycloakId, keycloakRoleId);

		Mockito.when(mockKeycloakUserService.updateUser(Mockito.any())).thenReturn(true);
		Mockito.when(mockKeycloakUserService.assignRole(Mockito.any(), Mockito.any())).thenReturn(false);

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
		Assertions.assertEquals("f376a0e2-d26c-4c66-beb9-e5d5d63cd687", updatedUser.getRole().getId());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldUpdateOnlyRole() {
		String keycloakId = "f1b1b1b1-1b1b-1b1b-1b1b-1b1b1b1b1b14";
		String keycloakRoleId= "13fbb108-1b6e-46f5-9e0d-d78f4bca1efc";
		UpdateUserEvent updateUserEvent = createUpdateUserEvent(keycloakId, keycloakRoleId);

		Mockito.when(mockKeycloakUserService.updateUser(Mockito.any())).thenReturn(false);
		Mockito.when(mockKeycloakUserService.assignRole(Mockito.any(), Mockito.any())).thenReturn(true);

		updateUserEventListener.handleUserUpdate(updateUserEvent);

		User updatedUser = userService.findByKeycloakId(keycloakId);
		Assertions.assertEquals("Artur", updatedUser.getName());
		Assertions.assertEquals("Kopek", updatedUser.getLastName());
		Assertions.assertEquals("artur55555@o2.pl", updatedUser.getEmail());
		Assertions.assertEquals("", updatedUser.getPhone());
		Assertions.assertEquals("Brzoskwiniowa 14/3", updatedUser.getAddressLine1());
		Assertions.assertEquals(Date.valueOf("1999-09-18"), updatedUser.getDayOfBirth());
		Assertions.assertEquals("41-800", updatedUser.getPostalCode());
		Assertions.assertEquals("Zabrze", updatedUser.getTown());
		Assertions.assertEquals("13fbb108-1b6e-46f5-9e0d-d78f4bca1efc", updatedUser.getRole().getId());
	}

	private UpdateUserEvent createUpdateUserEvent(String keycloakId, String roleId) {
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
				.setRoleId(roleId)
				.build();
	}
}
