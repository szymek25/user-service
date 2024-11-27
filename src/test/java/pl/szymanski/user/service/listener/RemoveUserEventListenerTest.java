package pl.szymanski.user.service.listener;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RemoveUserEventListenerTest {

	@Autowired
	private RemoveUserEventListener removeUserEventListener;

	@MockBean
	private KeycloakUserService keycloakUserService;

	@Autowired
	private UserService userService;

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldDeleteUser() {
		final String id = "aa183f01-9487-437e-9d40-6665286fd641";
		when(keycloakUserService.deleteUser(id)).thenReturn(true);

		removeUserEventListener.handleUserDeletion(new RemoveUserEvent(id));

		User byKeycloakId = userService.findByKeycloakId(id);
		assertNull(byKeycloakId);
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldNotDeleteUserIfFailedInKeycloak() {
		final String id = "aa183f01-9487-437e-9d40-6665286fd641";
		when(keycloakUserService.deleteUser(id)).thenReturn(false);

		removeUserEventListener.handleUserDeletion(new RemoveUserEvent(id));

		User byKeycloakId = userService.findByKeycloakId(id);
		assertNotNull(byKeycloakId);
	}
}
