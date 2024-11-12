package pl.szymanski.user.service.mapper.helper;

import io.swagger.client.model.UserRepresentation;
import pl.szymanski.user.service.model.Role;

import java.sql.Date;

public interface UserMapperHelper {

	String mapStringAttribute(UserRepresentation userRepresentation, String attributeName);

	Date mapBirthDate(UserRepresentation userRepresentation);

	Role mapRole(UserRepresentation userRepresentation);
}
