package pl.szymanski.user.service.oauth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakJwtRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	private static final String ROLES = "roles";

	/**
	 * Extracts the realm and resource level roles from a JWT token distinguishing between them using prefixes.
	 */
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		// Collection that will hold the extracted roles
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		Map<String, Object> claims = jwt.getClaims();

		if (claims.containsKey(ROLES)) {
			Collection<String> roles = (Collection<String>) claims.get(ROLES);
			grantedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
		}


		return grantedAuthorities;
	}

	Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
		return roles.stream().map(SimpleGrantedAuthority::new).collect(
				Collectors.toList());
	}
}
