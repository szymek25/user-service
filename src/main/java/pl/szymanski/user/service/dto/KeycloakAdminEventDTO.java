package pl.szymanski.user.service.dto;


import lombok.Data;

@Data
public class KeycloakAdminEventDTO {

	private String id;

	private String type;

	private String realmName;

	private String operationType;

	private String resourceType;

	private String representation;

	private String resourcePath;

}
