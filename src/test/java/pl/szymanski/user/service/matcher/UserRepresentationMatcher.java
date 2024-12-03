package pl.szymanski.user.service.matcher;

import io.swagger.client.model.UserRepresentation;
import org.mockito.ArgumentMatcher;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.model.User;

import java.util.List;
import java.util.Map;

public class UserRepresentationMatcher implements ArgumentMatcher<UserRepresentation> {

	private UserRepresentation userRepresentation;

	public UserRepresentationMatcher(final UserRepresentation userRepresentation) {
		this.userRepresentation = userRepresentation;
	}

	@Override
	public boolean matches(final UserRepresentation argument) {
		return userRepresentation.getId() != null ? userRepresentation.getId().equals(argument.getId()) : argument.getId() == null
				&& userRepresentation.getUsername().equals(argument.getUsername())
				&& userRepresentation.getFirstName().equals(argument.getFirstName())
				&& userRepresentation.getLastName().equals(argument.getLastName()) && userRepresentation.getEmail().equals(argument.getEmail())
				&& userRepresentation.isEnabled().equals(argument.isEnabled()) && userRepresentation.isEmailVerified().equals(argument.isEmailVerified())
				&& matchAttributes(argument) && matchGroups(argument);
	}

	private boolean matchGroups(UserRepresentation argument) {
		return argument.getGroups().equals(userRepresentation.getGroups());
	}

	private boolean matchAttributes(final UserRepresentation argument) {
		Map<String, List<String>> sourceAttributes = userRepresentation.getAttributes();
		Map<String, List<String>> targetAttributes = argument.getAttributes();
		return sourceAttributes.get(ApplicationConstants.KeyCloak.TOWN).equals(targetAttributes.get(ApplicationConstants.KeyCloak.TOWN))
				&& sourceAttributes.get(ApplicationConstants.KeyCloak.PHONE).equals(targetAttributes.get(ApplicationConstants.KeyCloak.PHONE))
				&& sourceAttributes.get(ApplicationConstants.KeyCloak.ADDRESS_LINE_1).equals(targetAttributes.get(ApplicationConstants.KeyCloak.ADDRESS_LINE_1))
				&& sourceAttributes.get(ApplicationConstants.KeyCloak.POSTAL_CODE).equals(targetAttributes.get(ApplicationConstants.KeyCloak.POSTAL_CODE))
				&& sourceAttributes.get(ApplicationConstants.KeyCloak.BIRTHDATE).equals(targetAttributes.get(ApplicationConstants.KeyCloak.BIRTHDATE));
	}

}
