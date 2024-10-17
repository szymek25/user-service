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
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.InitStateService;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		if (latestInitState == null || !latestInitState.isInitialized()) {
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
		List<Role> roleList = roleGroupRepresentationMapper.groupRepresentationsToRoles(groupRepresentations);
		roleService.saveAll(roleList);
		assignUsersToRoles(roleList);
	}

	private void assignUsersToRoles(List<Role> roleList) {
		LOG.info("Assigning users to roles");
		Map<Role, List<String>> roleToUserIdsMap = new HashMap<>();
		roleList.forEach(role -> {
			List<UserRepresentation> membersOfGroup = keycloakGroupService.getMembersOfGroup(role.getId());
			List<String> userIds = membersOfGroup.stream().map(UserRepresentation::getId).toList();
			roleToUserIdsMap.put(role, userIds);
		});
		roleToUserIdsMap.forEach((role, userIds) -> {
			List<User> users = userService.findAllById(userIds);
			users.forEach(user -> {
				if (user.getRole() == null) {
					user.setRole(role);
				} else {
					LOG.warn("User {} already has role {}. For proper processing please check keycloak settings", user.getId(), user.getRole().getName());
				}
			});
			userService.saveAll(users);
		});
		LOG.info("Users assigned to roles");
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
