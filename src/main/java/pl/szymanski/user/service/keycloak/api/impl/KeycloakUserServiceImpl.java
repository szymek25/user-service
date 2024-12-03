package pl.szymanski.user.service.keycloak.api.impl;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.UserApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.CredentialRepresentation;
import io.swagger.client.model.UserRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.keycloak.api.AbstractKeycloakService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class KeycloakUserServiceImpl extends AbstractKeycloakService implements KeycloakUserService {

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

	@NonNull
	private UsersApi usersApi;

	@NonNull
	private UserApi userApi;

	@NonNull
	private UserService userService;

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

	@Override
	public void updateUser(final UserRepresentation userRepresentation) {
		try {
			userApi.realmUsersIdPut(realm, userRepresentation.getId(), userRepresentation);
		} catch (ApiException e) {
			LOG.error("Error while posting user", e);
		}
	}

	@Override
	public void assignRole(String userId, String roleId) {
		final String currentRoleOfUser = userService.getCurrentRoleOfUser(userId);
		try {
			if (!currentRoleOfUser.equals(roleId)) {
				userApi.realmUsersIdGroupsGroupIdDelete(realm, userId, currentRoleOfUser);
				userApi.realmUsersIdGroupsGroupIdPut(realm, userId, roleId);
			}
		} catch (ApiException e) {
			LOG.error("Error while assigning role: {} for user: {}", roleId, userId, e);
		}
	}

	@Override
	public void deleteUser(String userId) {
		try {
			userApi.realmUsersIdDelete(realm, userId);
		} catch (ApiException e) {
			LOG.error("Error while deleting user: {}", userId, e);
		}
	}

	@Override
	public void updatePassword(String userId, String password) {
		final CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(ApplicationConstants.KeyCloak.CREDENTIAL_TYPE_PASSWORD);
		credential.setValue(password);
		try {
			userApi.realmUsersIdResetPasswordPut(realm, userId, credential);
		} catch (ApiException e) {
			LOG.error("Error while updating password for user: {}", userId, e);
		}
	}

	@Override
	public boolean createUser(UserRepresentation userRepresentation) throws ApiException {
		ApiResponse<Void> voidApiResponse = usersApi.realmUsersPostWithHttpInfo(realm, userRepresentation);
		return HttpStatus.OK.value() == voidApiResponse.getStatusCode();
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
