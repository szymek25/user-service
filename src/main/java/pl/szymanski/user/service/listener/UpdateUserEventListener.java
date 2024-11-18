package pl.szymanski.user.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

@Component
public class UpdateUserEventListener {

	private static final Logger log = LoggerFactory.getLogger(UpdateUserEventListener.class);
	@KafkaListener(topics = "user-updates", groupId = "user-service", containerFactory = "userUpdatesContainerFactor")
	public void handleUserUpdate(UpdateUserEvent event) {
		log.info("Received user update event: {}", event);
	}
}
