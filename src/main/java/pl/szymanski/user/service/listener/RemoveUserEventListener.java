package pl.szymanski.user.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.service.UserService;

@Component
public class RemoveUserEventListener {

	@Autowired
	private KeycloakUserService keycloakUserService;

	@KafkaListener(topics = "user-removes", groupId = "user-service", containerFactory = "userRemovesContainerFactor")
	public void handleUserDeletion(RemoveUserEvent event) {
		String id = event.getId();
		keycloakUserService.deleteUser(id);
	}
}
