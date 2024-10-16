package pl.szymanski.user.service.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessToken {

	@JsonProperty("access_token")
	private String tokenValue;
}
