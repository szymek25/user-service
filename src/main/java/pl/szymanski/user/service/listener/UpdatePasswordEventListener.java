package pl.szymanski.user.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdatePasswordEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;

@Component
public class UpdatePasswordEventListener {

	@Autowired
	private KeycloakUserService keycloakUserService;

	@KafkaListener(topics = "update-password", groupId = "user-service", containerFactory = "updatePasswordContainerFactory")
	public void handleUserUpdate(UpdatePasswordEvent event) {
		keycloakUserService.updatePassword(event.getId(), event.getValue());
	}
}
