package pl.szymanski.user.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.mapper.UserUpdateUserEventMapper;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

@Component
public class UpdateUserEventListener {

	@Autowired
	private UserUpdateUserEventMapper userUpdateUserEventMapper;

	@Autowired
	private UserService userService;
	private static final Logger log = LoggerFactory.getLogger(UpdateUserEventListener.class);

	@KafkaListener(topics = "user-updates", groupId = "user-service", containerFactory = "userUpdatesContainerFactor")
	public void handleUserUpdate(UpdateUserEvent event) {
		log.debug("Received user update event: {}", event);
		User userToBeUpdated = userUpdateUserEventMapper.map(event);
		userService.update(userToBeUpdated, event.getId());
	}
}
