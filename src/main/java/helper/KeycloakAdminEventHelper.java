package helper;

import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;

import java.util.Arrays;
import java.util.List;

public class KeycloakAdminEventHelper {

	private static final List<String> VALID_EVENT_TYPES = Arrays.asList(ApplicationConstants.KeyCloak.EVENT_TYPE_CREATE,
			ApplicationConstants.KeyCloak.EVENT_TYPE_UPDATE, ApplicationConstants.KeyCloak.EVENT_TYPE_DELETE);

	public static String getKeycloakUserIdFromEvent(final KeycloakAdminEventDTO event) {
		final String resourcePath = event.getResourcePath();
		try {
			return resourcePath.split("/")[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid resource path: %s".formatted(resourcePath));
		}
	}

	public static String getKeycloakGroupIdFromEvent(final KeycloakAdminEventDTO event) {
		final String resourcePath = event.getResourcePath();
		try {
			return resourcePath.split("/")[3];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid resource path: %s".formatted(resourcePath));
		}
	}

	public static boolean checkIfEventTypeIsDelete(final KeycloakAdminEventDTO event) {
		return ApplicationConstants.KeyCloak.EVENT_TYPE_DELETE.equals(event.getOperationType());
	}

	public static boolean checkIfEventTypeIsUpdateOrCreate(final KeycloakAdminEventDTO event) {
		return ApplicationConstants.KeyCloak.EVENT_TYPE_UPDATE.equals(event.getOperationType()) || ApplicationConstants.KeyCloak.EVENT_TYPE_CREATE.equals(event.getOperationType());
	}

	public static void validateEvent(final KeycloakAdminEventDTO event, String expectedResourceType) {
		if (!expectedResourceType.equals(event.getResourceType())) {
			throw new IllegalArgumentException("Resource type: %s is not supported, could not process event. Supported resource type is: %s".formatted(event.getResourceType(), ApplicationConstants.KeyCloak.RESOURCE_TYPE_USER));
		}
		if (!VALID_EVENT_TYPES.contains(event.getOperationType())) {
			throw new IllegalArgumentException("Operation type: %s is not valid, could not process event".formatted(event.getOperationType()));
		}
	}
}
