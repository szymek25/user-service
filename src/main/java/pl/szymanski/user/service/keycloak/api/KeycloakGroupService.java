package pl.szymanski.user.service.keycloak.api;

import io.swagger.client.model.GroupRepresentation;

import java.util.List;

public interface KeycloakGroupService {

	List<GroupRepresentation> getGroups();
}
