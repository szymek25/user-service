package pl.szymanski.user.service.oauth;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.keycloak.token.KeycloakTokenRequester;

import java.io.IOException;

@Component
public class AccessTokenForTechnicalCallsInterceptor implements Interceptor {

	@Autowired
	private KeycloakTokenRequester keycloakTokenRequester;

	@Override
	public Response intercept(Chain chain) throws IOException {

		Request request = chain.request();
		Request authenticatedRequest = request.newBuilder()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + keycloakTokenRequester.getAccessToken()).build();
		return chain.proceed(authenticatedRequest);
	}
}
