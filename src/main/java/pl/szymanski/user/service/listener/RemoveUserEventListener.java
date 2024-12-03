package pl.szymanski.user.service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;

import javax.annotation.Resource;

@Component
public class RemoveUserEventListener {

	@Resource(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@KafkaListener(topics = "user-removes", groupId = "user-service", containerFactory = "userRemovesContainerFactor")
	public void handleUserDeletion(RemoveUserEvent event) {
		String id = event.getId();
		keycloakUserService.deleteUser(id);
	}
}
