package pl.szymanski.user.service.keycloak.api;

import io.swagger.client.ApiException;
import io.swagger.client.api.UserApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.UserRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.szymanski.user.service.keycloak.api.impl.KeycloakUserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class KeycloakUserServiceImplTest {

	@InjectMocks
	private KeycloakUserServiceImpl keycloakUserService;

	@Mock
	private UsersApi usersApi;

	@Mock
	private UserApi userApi;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(keycloakUserService);
	}


	@Test
	public void testGetTotalNumberOfUsers() throws ApiException {
		when(usersApi.realmUsersCountGet(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(10);
		int totalNumberOfUsers = keycloakUserService.getTotalNumberOfUsers();
		assertEquals(10, totalNumberOfUsers);
	}

	@Test
	public void getTotalNumberOfUsersShouldReturnZeroOnApiException() throws ApiException {
		when(usersApi.realmUsersCountGet(any(), any(), any(), any(), any(), any(), any(), any())).thenThrow(new ApiException(500, "Internal Server Error"));
		int totalNumberOfUsers = keycloakUserService.getTotalNumberOfUsers();
		assertEquals(0, totalNumberOfUsers);
	}

	@Test
	public void testGetUsersCallCount() throws ApiException {
		// Mock the UsersApi
		when(keycloakUserService.getTotalNumberOfUsers()).thenReturn(13);

		ReflectionTestUtils.setField(keycloakUserService, "pageSize", 5);
		keycloakUserService.getUsers();
		Mockito.verify(usersApi, Mockito.times(3)).realmUsersGet(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), any(), any(), any(), any());

		clearInvocations(usersApi);
		ReflectionTestUtils.setField(keycloakUserService, "pageSize", 15);
		keycloakUserService.getUsers();
		Mockito.verify(usersApi, Mockito.times(1)).realmUsersGet(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), any(), any(), any(), any());

		when(keycloakUserService.getTotalNumberOfUsers()).thenReturn(0);
		List<UserRepresentation> users = keycloakUserService.getUsers();
		assertEquals(users, List.of());
	}

	@Test
	public void getUsersShouldReturnEmptyListOnApiException() throws ApiException {
		when(keycloakUserService.getTotalNumberOfUsers()).thenReturn(10);
		ReflectionTestUtils.setField(keycloakUserService, "pageSize", 15);
		when(usersApi.realmUsersGet(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), any(), any(), any(), any())).thenThrow(new ApiException(500, "Internal Server Error"));
		List<UserRepresentation> users = keycloakUserService.getUsers();
		assertEquals(users, List.of());
	}
}