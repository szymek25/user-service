package pl.szymanski.user.service.matcher;

import org.mockito.ArgumentMatcher;
import pl.szymanski.user.service.model.User;

public class UserMatcher implements ArgumentMatcher<User> {

	private User user;

	public UserMatcher(final User user) {
		this.user = user;
	}

	@Override
	public boolean matches(final User argument) {
		return user.getKeycloakId().equals(argument.getKeycloakId()) && user.getName().equals(argument.getName())
				&& user.getLastName().equals(argument.getLastName()) && user.getEmail().equals(argument.getEmail())
				&& user.getPhone().equals(argument.getPhone()) && user.getTown().equals(argument.getTown())
				&& user.getDayOfBirth().equals(argument.getDayOfBirth()) && user.getAddressLine1().equals(argument.getAddressLine1());
	}

}
