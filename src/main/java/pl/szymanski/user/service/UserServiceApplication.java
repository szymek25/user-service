package pl.szymanski.user.service;

import io.swagger.client.ApiClient;
import io.swagger.client.api.GroupApi;
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
	public GroupApi groupApi() {
		GroupApi apiInstance = new GroupApi();
		ApiClient defaultClient = apiInstance.getApiClient();
		defaultClient.setBasePath(keycloakAdminApiPath);
		defaultClient.getHttpClient().networkInterceptors().add(accessTokenInterceptor());

		return apiInstance;
	}

	@Bean
	public AccessTokenInterceptor accessTokenInterceptor() {
		return new AccessTokenInterceptor();
	}
}
