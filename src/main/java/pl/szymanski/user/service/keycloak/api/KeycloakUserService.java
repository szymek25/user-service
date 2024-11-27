package pl.szymanski.user.service.keycloak.api;

import io.swagger.client.model.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

	int getTotalNumberOfUsers();

	List<UserRepresentation> getUsers();

	void updateUser(UserRepresentation userRepresentation);

	void assignRole(String userId, String roleId);

	void deleteUser(String userId);
}
