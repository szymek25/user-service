package pl.szymanski.user.service.mapper.helper;

import io.swagger.client.model.UserRepresentation;

import java.sql.Date;

public interface UserMapperHelper {

	String mapStringAttribute(UserRepresentation userRepresentation, String attributeName);

	Date mapBirthDate(UserRepresentation userRepresentation);
}
