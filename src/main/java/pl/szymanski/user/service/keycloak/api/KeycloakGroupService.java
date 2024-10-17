package pl.szymanski.user.service.keycloak.api;

import io.swagger.client.model.GroupRepresentation;
import io.swagger.client.model.UserRepresentation;

import java.util.List;

public interface KeycloakGroupService {

	List<GroupRepresentation> getGroups();

	List<UserRepresentation> getMembersOfGroup(String groupId);
}
