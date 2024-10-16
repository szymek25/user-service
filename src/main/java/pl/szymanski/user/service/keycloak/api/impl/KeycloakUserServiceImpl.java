package pl.szymanski.user.service.keycloak.api.impl;

import io.swagger.client.ApiException;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.UserRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import pl.szymanski.user.service.keycloak.api.AbstractKeycloakService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class KeycloakUserServiceImpl extends AbstractKeycloakService implements KeycloakUserService {

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

	@NonNull
	private UsersApi usersApi;

	@Value("${keycloak.users.pageSize}")
	private int pageSize;

	@Override
	public int getTotalNumberOfUsers() {
		try {
			return usersApi.realmUsersCountGet(realm, null, null, null, null, null, null, null);
		} catch (ApiException e) {
			LOG.error("Error while getting total number of users", e);
			return 0;
		}
	}

	@Override
	public List<UserRepresentation> getUsers() {
		int totalNumberOfUsers = getTotalNumberOfUsers();
		if (totalNumberOfUsers == 0) {
			return List.of();
		} else if (totalNumberOfUsers < pageSize) {
			return getUsers(0, totalNumberOfUsers);
		} else {
			final List<UserRepresentation> users = new ArrayList<>();
			for (int i = 0; i < totalNumberOfUsers; i += pageSize) {
				users.addAll(getUsers(i, i + pageSize));
			}
			return users;
		}
	}

	private List<UserRepresentation> getUsers(int first, int max) {
		try {
			return usersApi.realmUsersGet(realm, null, null, null, null, null, null, null, null, first, max, null, null, null, null);
		} catch (ApiException e) {
			LOG.error("Error while getting users", e);
			return List.of();
		}
	}
}
