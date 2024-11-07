package pl.szymanski.user.service.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.KeycloakAdminEventDTO;
import pl.szymanski.user.service.facade.KeycloakGroupFacade;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

import static helper.KeycloakAdminEventHelper.checkIfEventTypeIsDelete;
import static helper.KeycloakAdminEventHelper.checkIfEventTypeIsUpdateOrCreate;
import static helper.KeycloakAdminEventHelper.getKeycloakGroupIdFromEvent;
import static helper.KeycloakAdminEventHelper.getKeycloakUserIdFromEvent;
import static helper.KeycloakAdminEventHelper.validateEvent;

@Component
public class KeycloakGroupFacadeImpl implements KeycloakGroupFacade {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;


	@Override
	public void processGroupUpdate(final KeycloakAdminEventDTO event) {
		validateEvent(event, ApplicationConstants.KeyCloak.RESOURCE_TYPE_GROUP_MEMBERSHIP);
		final String keycloakUserIdFromEvent = getKeycloakUserIdFromEvent(event);
		final User user = userService.findByKeycloakId(keycloakUserIdFromEvent);
		if (checkIfEventTypeIsUpdateOrCreate(event)) {
			final Role role = roleService.getById(getKeycloakGroupIdFromEvent(event));
			userService.assignRole(user, role);
		} else if (checkIfEventTypeIsDelete(event)) {
			userService.assignRole(user, null);
		}
	}


}
