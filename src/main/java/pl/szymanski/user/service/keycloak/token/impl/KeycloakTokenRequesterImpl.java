package pl.szymanski.user.service.keycloak.token.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.szymanski.user.service.keycloak.model.AccessToken;
import pl.szymanski.user.service.keycloak.token.KeycloakTokenRequester;

import java.util.Objects;

@Component
public class KeycloakTokenRequesterImpl implements KeycloakTokenRequester {

	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String GRANT_TYPE = "grant_type";
	private static final String CLIENT_CREDENTIALS = "client_credentials";
	@Value("${keycloak.tokenUri}")
	private String tokenUri;

	@Value("${keycloak.client-id}")
	private String clientId;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	private String accessToken;

	public String getAccessToken() {
		if (StringUtils.isEmpty(accessToken)) {
			final RestTemplate restTemplate = new RestTemplate();
			HttpEntity<MultiValueMap<String, String>> request = prepareRequest();

			final ResponseEntity<AccessToken> result = restTemplate.postForEntity(tokenUri, request, AccessToken.class);

			String tokenValue = Objects.requireNonNull(result.getBody()).getTokenValue();
			this.accessToken = tokenValue;
			return tokenValue;
		} else {
			return accessToken;
		}
	}

	private HttpEntity<MultiValueMap<String, String>> prepareRequest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = prepareRequestParams();

		return new HttpEntity<>(map, headers);
	}

	private MultiValueMap<String, String> prepareRequestParams() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add(CLIENT_ID, clientId);
		map.add(CLIENT_SECRET, clientSecret);
		map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
		return map;
	}
}
