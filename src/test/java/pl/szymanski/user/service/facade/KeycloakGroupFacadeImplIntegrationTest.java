package pl.szymanski.user.service.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class KeycloakGroupFacadeImplIntegrationTest {

	private static final String USER_ID = "e6c8cdf1-34a3-4d8c-a459-098f8ca2b5d1";
	private static final String GROUP_ID = "ca704dd5-067d-4847-a9b2-06c91c4b8744";
	@Autowired
	private KeycloakGroupFacade keycloakGroupFacade;

	@Autowired
	private UserService userService;

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldAssignUserToGroup() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("CREATE");

		keycloakGroupFacade.processGroupUpdate(eventDTO);

		User user = userService.findByKeycloakId(USER_ID);
		assertEquals(GROUP_ID, user.getRole().getId());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldRemoveUserFromGroup() {
		KeycloakAdminEventDTO eventDTO = prepareKeycloakAdminEventDTO("DELETE");

		keycloakGroupFacade.processGroupUpdate(eventDTO);

		User user = userService.findByKeycloakId(USER_ID);
		assertNull(user.getRole());
	}

	private KeycloakAdminEventDTO prepareKeycloakAdminEventDTO(String operationType) {
		KeycloakAdminEventDTO keycloakAdminEventDTO = new KeycloakAdminEventDTO();
		keycloakAdminEventDTO.setOperationType(operationType);
		keycloakAdminEventDTO.setResourceType("GROUP_MEMBERSHIP");
		keycloakAdminEventDTO.setResourcePath("users/" + USER_ID + "/groups/" + GROUP_ID);

		return keycloakAdminEventDTO;
	}
}
