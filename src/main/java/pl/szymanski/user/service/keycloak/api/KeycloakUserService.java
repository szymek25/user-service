package pl.szymanski.user.service.keycloak.api;

import io.swagger.client.model.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

	int getTotalNumberOfUsers();

	List<UserRepresentation> getUsers();
}
