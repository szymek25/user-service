package pl.szymanski.user.service.keycloak.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.KeycloakUserFacade;

import static pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.RESOURCE_TYPE_USER;

@Component
@KafkaListener(topics = "admin_events", groupId = "user-service")
public class KeycloakAdminEventsListener {
	private static final Logger log = LoggerFactory.getLogger(KeycloakAdminEventsListener.class);

	@Autowired
	private KeycloakUserFacade keycloakUserFacade;

	@KafkaHandler(isDefault = true)
	public void defaultEventHandler(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			KeycloakAdminEventDTO adminEvent = objectMapper.readValue(String.valueOf(object), KeycloakAdminEventDTO.class);
			log.debug("Keycloak event received: {}", adminEvent);
			if (RESOURCE_TYPE_USER.equals(adminEvent.getResourceType())) {
				keycloakUserFacade.updateOrCreateUser(adminEvent);
			}
		} catch (JsonProcessingException f) {
			log.warn("Unknown type received: " + object, f);
		}
	}

}
