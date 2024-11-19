package pl.szymanski.user.service.mapper.helper;

import io.swagger.client.model.UserRepresentation;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface UserMapperHelper {

	String mapStringAttribute(UserRepresentation userRepresentation, String attributeName);

	Date mapBirthDate(UserRepresentation userRepresentation);

	Role mapRole(UserRepresentation userRepresentation);

	Map<String, List<String>> mapAttributes(User user);

	List<String> mapRoles(User user);
}
