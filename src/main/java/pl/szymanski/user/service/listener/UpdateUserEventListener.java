package pl.szymanski.user.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.mapper.UserMapper;
import pl.szymanski.user.service.mapper.UserUpdateUserEventMapper;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import javax.annotation.Resource;

@Component
public class UpdateUserEventListener {

	@Autowired
	private UserUpdateUserEventMapper userUpdateUserEventMapper;

	@Autowired
	private UserService userService;

	@Resource(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@Autowired
	private UserMapper userMapper;
	private static final Logger log = LoggerFactory.getLogger(UpdateUserEventListener.class);

	@KafkaListener(topics = "user-updates", groupId = "user-service", containerFactory = "userUpdatesContainerFactor")
	public void handleUserUpdate(UpdateUserEvent event) {
		log.debug("Received user update event: {}", event);
		User userToBeUpdated = userUpdateUserEventMapper.map(event);
		boolean isUserUpdated = keycloakUserService.updateUser(userMapper.mapToUserRepresentation(userToBeUpdated));
		if (isUserUpdated) {
			userService.update(userToBeUpdated, event.getId());
		}
	}
}
