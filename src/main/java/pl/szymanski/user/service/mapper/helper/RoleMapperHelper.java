package pl.szymanski.user.service.mapper.helper;

import io.swagger.client.model.GroupRepresentation;

public interface RoleMapperHelper {

	String mapStringAttribute(GroupRepresentation userRepresentation, String attributeName);
}
