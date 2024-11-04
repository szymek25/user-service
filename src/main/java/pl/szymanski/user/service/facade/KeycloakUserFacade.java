package pl.szymanski.user.service.facade;

import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;

public interface KeycloakUserFacade {

	void updateOrCreateUser(KeycloakAdminEventDTO event);

}
