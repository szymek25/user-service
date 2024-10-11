package pl.szymanski.user.service;

import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.api.GroupApi;
import io.swagger.client.api.UsersApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.szymanski.user.service.oauth.AccessTokenInterceptor;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}


	@Value("${keycloak.admin-api-path}")
	private String keycloakAdminApiPath;

	@Bean
	public ApiClient apiClient() {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		defaultClient.setBasePath(keycloakAdminApiPath);
		defaultClient.getHttpClient().networkInterceptors().add(accessTokenInterceptor());

		return defaultClient;
	}

	@Bean
	public AccessTokenInterceptor accessTokenInterceptor() {
		return new AccessTokenInterceptor();
	}

	@Bean
	public GroupApi groupApi(ApiClient apiClient) {
		GroupApi apiInstance = new GroupApi(apiClient);
		return apiInstance;
	}

	@Bean
	public UsersApi usersApi(ApiClient apiClient) {
		UsersApi apiInstance = new UsersApi(apiClient);
		return apiInstance;
	}
}
