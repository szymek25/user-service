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

import java.util.Arrays;
import java.util.List;

@Component
public class KeycloakUserFacadeImpl implements KeycloakUserFacade {

	private static final List<String> VALID_EVENT_TYPES = Arrays.asList(ApplicationConstants.KeyCloak.EVENT_TYPE_CREATE,
			ApplicationConstants.KeyCloak.EVENT_TYPE_UPDATE, ApplicationConstants.KeyCloak.EVENT_TYPE_DELETE);

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Override
	public void processUserUpdate(final KeycloakAdminEventDTO event) {
		validateEvent(event);

		final User user = mapUserToObject(event);
		if (checkIfEventTypeIsUpdateOrCreate(event)) {
			final User existingUser = userService.findByKeycloakId(user.getKeycloakId());
			if (existingUser == null) {
				userService.save(user);
			} else {
				updateUser(existingUser, user);
				userService.save(existingUser);
			}
		} else if (checkIfEventTypeIsDelete(event)) {
			userService.delete(user.getKeycloakId());
		}


	}

	private static void validateEvent(final KeycloakAdminEventDTO event) {
		if (!ApplicationConstants.KeyCloak.RESOURCE_TYPE_USER.equals(event.getResourceType())) {
			throw new IllegalArgumentException("Resource type: %s is not supported, could not process event. Supported resource type is: %s".formatted(event.getResourceType(), ApplicationConstants.KeyCloak.RESOURCE_TYPE_USER));
		}
		if (!VALID_EVENT_TYPES.contains(event.getOperationType())) {
			throw new IllegalArgumentException("Operation type: %s is not valid, could not process event".formatted(event.getOperationType()));
		}
	}

	private static boolean checkIfEventTypeIsDelete(final KeycloakAdminEventDTO event) {
		return ApplicationConstants.KeyCloak.EVENT_TYPE_DELETE.equals(event.getOperationType());
	}

	private static boolean checkIfEventTypeIsUpdateOrCreate(final KeycloakAdminEventDTO event) {
		return ApplicationConstants.KeyCloak.EVENT_TYPE_UPDATE.equals(event.getOperationType()) || ApplicationConstants.KeyCloak.EVENT_TYPE_CREATE.equals(event.getOperationType());
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
			final String keycloakId = getKeycloakIdFromEvent(event);
			mappedUser.setKeycloakId(keycloakId);
			return mappedUser;
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Could not map user from event: %s".formatted(event), e);
		}
	}

	private String getKeycloakIdFromEvent(final KeycloakAdminEventDTO event) {
		final String resourcePath = event.getResourcePath();
		return resourcePath.split("/")[1];
	}
}
