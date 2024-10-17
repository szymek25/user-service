package pl.szymanski.user.service.keycloak.api.impl;

import io.swagger.client.ApiException;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.model.GroupRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.szymanski.user.service.keycloak.api.AbstractKeycloakService;
import pl.szymanski.user.service.keycloak.api.KeycloakGroupService;

import java.util.List;

@RequiredArgsConstructor
public class KeycloakGroupServiceImpl extends AbstractKeycloakService implements KeycloakGroupService {

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakGroupServiceImpl.class);

	@NonNull
	private GroupsApi groupsApi;


	@Override
	public List<GroupRepresentation> getGroups() {
		try {
			return groupsApi.realmGroupsGet(realm, null, null, null, false);
		} catch (ApiException e) {
			LOG.error("Error while getting groups", e);
		}
		return List.of();
	}
}
