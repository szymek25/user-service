package pl.szymanski.user.service.oauth;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessTokenInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
		Request request = chain.request();
		Request authenticatedRequest = request.newBuilder()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthToken.getToken().getTokenValue()).build();
		return chain.proceed(authenticatedRequest);
	}
}
