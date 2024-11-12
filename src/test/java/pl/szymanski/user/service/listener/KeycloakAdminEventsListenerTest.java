package pl.szymanski.user.service.listener;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.KeycloakGroupFacade;
import pl.szymanski.user.service.facade.KeycloakUserFacade;
import pl.szymanski.user.service.keycloak.listener.KeycloakAdminEventsListener;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class KeycloakAdminEventsListenerTest {

	@InjectMocks
	private KeycloakAdminEventsListener keycloakAdminEventsListener;

	@Mock
	private KeycloakUserFacade keycloakUserFacade;

	@Mock
	private KeycloakGroupFacade keycloakGroupFacade;

	@Test
	public void shouldProcessIfResourceTypeIsUser() {
		Map<String, String> map = new HashMap<>();
		map.put("resourceType", "USER");
		keycloakAdminEventsListener.defaultEventHandler(new JSONObject(map));

		Mockito.verify(keycloakUserFacade, Mockito.times(1)).processUserUpdate(Mockito.any(KeycloakAdminEventDTO.class));
	}

	@Test
	public void shouldNotProcessIfResourceTypeIsNotUser() {
		Map<String, String> map = new HashMap<>();
		map.put("resourceType", "NOT_USER");
		keycloakAdminEventsListener.defaultEventHandler(new JSONObject(map));

		Mockito.verify(keycloakUserFacade, Mockito.times(0)).processUserUpdate(Mockito.any(KeycloakAdminEventDTO.class));
		Mockito.verify(keycloakGroupFacade, Mockito.times(0)).processGroupUpdate(Mockito.any(KeycloakAdminEventDTO.class));
	}

	@Test
	public void shouldProcessIfResourceTypeIsGroupMembership() {
		Map<String, String> map = new HashMap<>();
		map.put("resourceType", "GROUP_MEMBERSHIP");
		keycloakAdminEventsListener.defaultEventHandler(new JSONObject(map));

		Mockito.verify(keycloakGroupFacade, Mockito.times(1)).processGroupUpdate(Mockito.any(KeycloakAdminEventDTO.class));
	}
}
