package pl.szymanski.user.service.facade;

import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;

public interface KeycloakGroupFacade {

	void processGroupUpdate(KeycloakAdminEventDTO event);
}
