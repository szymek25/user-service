package pl.szymanski.user.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import pl.szymanski.user.service.oauth.KeycloakJwtRolesConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String MANAGER = "MANAGER";
	private static final String EMPLOYEE = "EMPLOYEE";

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((request) -> request
						.anyRequest().hasAnyRole(MANAGER, EMPLOYEE)
//						.anyRequest().authenticated()
				);

		DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
				// Using the delegating converter multiple converters can be combined
				new DelegatingJwtGrantedAuthoritiesConverter(
						// First add the default converter
						new JwtGrantedAuthoritiesConverter(),
						// Second add our custom Keycloak specific converter
						new KeycloakJwtRolesConverter());

		http.oauth2ResourceServer(e -> e.jwt(j -> {
			j.jwtAuthenticationConverter(jwt -> new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt)));
		}));

		return http.build();
	}

}
