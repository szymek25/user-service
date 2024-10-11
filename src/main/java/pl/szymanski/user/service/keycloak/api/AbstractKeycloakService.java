package pl.szymanski.user.service.keycloak.api;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractKeycloakService {

	@Value("${keycloak.realm}")
	protected String realm;

}
