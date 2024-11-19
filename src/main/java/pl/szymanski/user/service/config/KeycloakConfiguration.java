package pl.szymanski.user.service.config;

import io.swagger.client.ApiClient;
import io.swagger.client.api.GroupApi;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.api.UserApi;
import io.swagger.client.api.UsersApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymanski.user.service.keycloak.api.KeycloakGroupService;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.keycloak.api.impl.KeycloakGroupServiceImpl;
import pl.szymanski.user.service.keycloak.api.impl.KeycloakUserServiceImpl;
import pl.szymanski.user.service.oauth.AccessTokenForTechnicalCallsInterceptor;
import pl.szymanski.user.service.oauth.AccessTokenFromCurrentRequestInterceptor;

@Configuration
public class KeycloakConfiguration {


	@Value("${keycloak.admin-api-path}")
	private String keycloakAdminApiPath;

	@Bean
	public ApiClient apiClientForApiCalls() {
		ApiClient defaultClient = new ApiClient();
		defaultClient.setBasePath(keycloakAdminApiPath);
		defaultClient.getHttpClient().networkInterceptors().add(accessTokenInterceptor());

		return defaultClient;
	}

	@Bean(name = "apiClientForTechnicalCalls")
	public ApiClient apiClientForTechnicalCalls(AccessTokenForTechnicalCallsInterceptor interceptor) {
		ApiClient defaultClient = new ApiClient();
		defaultClient.setBasePath(keycloakAdminApiPath);
		defaultClient.getHttpClient().networkInterceptors().add(interceptor);

		return defaultClient;
	}

	@Bean
	public AccessTokenFromCurrentRequestInterceptor accessTokenInterceptor() {
		return new AccessTokenFromCurrentRequestInterceptor();
	}

	@Bean
	public GroupsApi groupsApi(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new GroupsApi(apiClientForTechnicalCalls(interceptor));
	}

	@Bean("usersApiForApiCalls")
	public UsersApi usersApi() {
		return new UsersApi(apiClientForApiCalls());
	}

	@Bean("usersApiForTechnicalCalls")
	public UsersApi usersApiForTechnicalCalls(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new UsersApi(apiClientForTechnicalCalls(interceptor));
	}

	@Bean("userApiForApiCalls")
	public UserApi userApi() {
		return new UserApi(apiClientForApiCalls());
	}

	@Bean("userApiForTechnicalCalls")
	public UserApi userApiForTechnicalCalls(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new UserApi(apiClientForTechnicalCalls(interceptor));
	}

	@Bean("keycloakUserServiceForTechnicalCalls")
	public KeycloakUserService keycloakUserService(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new KeycloakUserServiceImpl(usersApiForTechnicalCalls(interceptor), userApiForTechnicalCalls(interceptor));
	}

	@Bean("keycloakGroupServiceForTechnicalCalls")
	public KeycloakGroupService keycloakGroupServiceForTechnicalCalls(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new KeycloakGroupServiceImpl(groupsApi(interceptor), groupApi(interceptor));
	}

	@Bean
	public GroupApi groupApi(AccessTokenForTechnicalCallsInterceptor interceptor) {
		return new GroupApi(apiClientForTechnicalCalls(interceptor));
	}

}
