package pl.szymanski.user.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdatePasswordEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import javax.annotation.Resource;

@Component
public class UpdatePasswordEventListener {

	@Resource(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@Autowired
	private UserService userService;

	@KafkaListener(topics = "update-password", groupId = "user-service", containerFactory = "updatePasswordContainerFactory")
	public void handleUserUpdate(UpdatePasswordEvent event) {
		User user = userService.findById(event.getId());
		keycloakUserService.updatePassword(user.getKeycloakId(), event.getValue());
	}
}
