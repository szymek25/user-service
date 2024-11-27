package pl.szymanski.user.service.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.KeycloakUserFacade;
import pl.szymanski.user.service.mapper.UserMapper;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import static helper.KeycloakAdminEventHelper.checkIfEventTypeIsDelete;
import static helper.KeycloakAdminEventHelper.checkIfEventTypeIsUpdateOrCreate;
import static helper.KeycloakAdminEventHelper.getKeycloakUserIdFromEvent;
import static helper.KeycloakAdminEventHelper.validateEvent;

@Component
public class KeycloakUserFacadeImpl implements KeycloakUserFacade {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Override
	public void processUserUpdate(final KeycloakAdminEventDTO event) {
		validateEvent(event, ApplicationConstants.KeyCloak.RESOURCE_TYPE_USER);

		if (checkIfEventTypeIsUpdateOrCreate(event)) {
			final User user = mapUserToObject(event);
			final User existingUser = userService.findByKeycloakId(user.getKeycloakId());
			if (existingUser == null) {
				userService.save(user);
			} else {
				updateUser(existingUser, user);
				userService.save(existingUser);
			}
		} else if (checkIfEventTypeIsDelete(event)) {
			String keycloakId = getKeycloakUserIdFromEvent(event);
			userService.delete(keycloakId);
		}


	}

	private void updateUser(final User existingUser, final User user) {
		existingUser.setAddressLine1(user.getAddressLine1());
		existingUser.setDayOfBirth(user.getDayOfBirth());
		existingUser.setEmail(user.getEmail());
		existingUser.setLastName(user.getLastName());
		existingUser.setName(user.getName());
		existingUser.setPhone(user.getPhone());
		existingUser.setTown(user.getTown());
		existingUser.setPostalCode(user.getPostalCode());
	}

	private User mapUserToObject(final KeycloakAdminEventDTO event) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			final UserRepresentation userRepresentation = mapper.readValue(event.getRepresentation(), UserRepresentation.class);
			final User mappedUser = userMapper.map(userRepresentation);
			final String keycloakId = getKeycloakUserIdFromEvent(event);
			mappedUser.setKeycloakId(keycloakId);
			return mappedUser;
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Could not map user from event: %s".formatted(event), e);
		}
	}

}
