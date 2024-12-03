package pl.szymanski.user.service.init;

import io.swagger.client.model.GroupRepresentation;
import io.swagger.client.model.UserRepresentation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dao.InitStateDao;
import pl.szymanski.user.service.dao.UserDao;
import pl.szymanski.user.service.keycloak.api.KeycloakGroupService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.model.InitState;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.matches;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"user-service.initial-load.enabled=true"})
@ExtendWith(OutputCaptureExtension.class)
public class DataBaseInitializerIntegrationTest {

	private static final String GROUP_1_ID = "1a-1b";
	private static final String GROUP_2_ID = "2a-2b";

	private static final List<UserRepresentation> SAMPLE_USERS = getSampleUsers();
	private static final String USER_1_ID = "1e-1e-1e";
	private static final String USER_2_ID = "2e-2e-2e";

	@Autowired
	private DataBaseInitializer dataBaseInitializer;

	@Autowired
	private InitStateDao initStateDao;

	@MockBean(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@MockBean
	private KeycloakGroupService keycloakGroupService;

	@Autowired
	private UserDao userDao;

	@BeforeEach
	public void setUp() {
		initStateDao.deleteAll();
		when(keycloakUserService.getTotalNumberOfUsers()).thenReturn(2);
		when(keycloakUserService.getUsers()).thenReturn(SAMPLE_USERS);
		when(keycloakGroupService.getGroups()).thenReturn(getSampleGroups());
		when(keycloakGroupService.getMembersOfGroup(matches(GROUP_1_ID))).thenReturn(Collections.singletonList(SAMPLE_USERS.get(0)));
		when(keycloakGroupService.getMembersOfGroup(matches(GROUP_2_ID))).thenReturn(Collections.singletonList(SAMPLE_USERS.get(1)));
	}

	@Test
	public void shouldNotInitializeDataBaseIfLatestEntryIsInitialized(CapturedOutput output) {
		// given
		generate2InitStatesLatestIsInitialized();
		// when
		dataBaseInitializer.initializeDBAfterStartup();
		// then
		assertTrue(output.getOut().contains("DB already initialized"));
	}

	@Test
	void shouldInitializeDataBaseState() {
		generate2InitStatesLatestIsNotInitialized();

		dataBaseInitializer.initializeDBAfterStartup();

		ArrayList<InitState> initStates = Lists.newArrayList(initStateDao.findAll());
		initStates.sort(Comparator.comparing(InitState::getId).reversed()); //take init state with highest id
		InitState initState = initStates.get(0);
		assertTrue(initState.isInitialized());

		List<User> users = userDao.findAllByKeycloakId(List.of(USER_1_ID, USER_2_ID));
		assertEquals(2, users.size());
		User user1 = users.get(0);
		assertEquals(USER_1_ID, user1.getKeycloakId());
		assertEquals(user1.getEmail(), user1.getEmail());
		Role roleOfUser1 = user1.getRole();
		assertEquals(GROUP_1_ID, roleOfUser1.getId());
		assertEquals("testDescription", roleOfUser1.getDescription());
	}


	private void generate2InitStatesLatestIsInitialized() {
		InitState initState1 = new InitState();
		initState1.setInitialized(false);
		long currentTime = System.currentTimeMillis();
		initState1.setLastUpdate(currentTime);

		InitState initState2 = new InitState();
		initState2.setInitialized(true);
		initState2.setLastUpdate(currentTime + 1000);

		initStateDao.saveAll(List.of(initState1, initState2));
	}

	private void generate2InitStatesLatestIsNotInitialized() {
		InitState initState1 = new InitState();
		initState1.setInitialized(true);
		long currentTime = System.currentTimeMillis();
		initState1.setLastUpdate(currentTime);

		InitState initState2 = new InitState();
		initState2.setInitialized(false);
		initState2.setLastUpdate(currentTime + 1000);

		initStateDao.saveAll(List.of(initState1, initState2));
	}

	private List<GroupRepresentation> getSampleGroups() {
		List<GroupRepresentation> groups = new ArrayList<>();
		GroupRepresentation group1 = new GroupRepresentation();
		group1.setId(GROUP_1_ID);
		group1.setName("group1");
		GroupRepresentation group2 = new GroupRepresentation();
		group2.setId(GROUP_2_ID);
		group2.setName("group2");
		groups.add(group1);
		groups.add(group2);
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put(ApplicationConstants.KeyCloak.DESCRIPTION, Arrays.asList("testDescription"));
		group1.setAttributes(attributes);
		return groups;
	}

	private static List<UserRepresentation> getSampleUsers() {
		List<UserRepresentation> users = new ArrayList<>();
		UserRepresentation user1 = new UserRepresentation();
		user1.setId(USER_1_ID);
		user1.setEmail("test@test.com");
		user1.setUsername("test");
		UserRepresentation user2 = new UserRepresentation();
		user2.setId(USER_2_ID);
		user2.setEmail("test1@test.com");
		user2.setUsername("test1");
		users.add(user1);
		users.add(user2);
		return users;
	}
}
