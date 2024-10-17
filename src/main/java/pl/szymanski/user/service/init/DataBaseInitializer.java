package pl.szymanski.user.service.init;

import io.swagger.client.model.GroupRepresentation;
import io.swagger.client.model.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.keycloak.api.KeycloakGroupService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.mapper.RoleGroupRepresentationMapper;
import pl.szymanski.user.service.mapper.UserMapper;
import pl.szymanski.user.service.model.InitState;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.InitStateService;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DataBaseInitializer {
	private final Logger LOG = LoggerFactory.getLogger(DataBaseInitializer.class);

	@Autowired
	private InitStateService initStateService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleGroupRepresentationMapper roleGroupRepresentationMapper;

	@Resource(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@Resource(name = "keycloakGroupServiceForTechnicalCalls")
	private KeycloakGroupService keycloakGroupService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		InitState latestInitState = initStateService.getLatestInitState();
		if(latestInitState == null || !latestInitState.isInitialized()) {
			LOG.info("Starting initialization of DB");
			storeUsers();
			storeGroups();
			saveInitState();
			LOG.info("DB initialized");
		} else {
			LOG.info("DB already initialized");
		}
	}

	private void storeGroups() {
		LOG.info("Dropping roles table");
		roleService.deleteAll();
		List<GroupRepresentation> groupRepresentations = keycloakGroupService.getGroups();
		LOG.info("Saving groups to DB");
		roleService.saveAll(roleGroupRepresentationMapper.groupRepresentationsToRoles(groupRepresentations));
	}

	private void saveInitState() {
		InitState initState = new InitState();
		initState.setInitialized(true);
		initState.setLastUpdate(System.currentTimeMillis());
		initStateService.save(initState);
	}

	private void storeUsers() {
		List<UserRepresentation> users = keycloakUserService.getUsers();
		List<User> map = userMapper.map(users);
		LOG.info("Dropping users table");
		userService.deleteAll();
		LOG.info("Saving users to DB");
		userService.saveAll(map);
	}
}
