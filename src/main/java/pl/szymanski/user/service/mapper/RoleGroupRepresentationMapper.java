package pl.szymanski.user.service.mapper;

import io.swagger.client.model.GroupRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.mapper.helper.RoleMapperHelper;
import pl.szymanski.user.service.model.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoleGroupRepresentationMapper {

	@Autowired
	protected RoleMapperHelper helper;

	@Mapping(target = "description", expression = "java(helper.mapStringAttribute(groupRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.DESCRIPTION))")
	public abstract Role groupRepresentationToRole(GroupRepresentation groupRepresentation);

	public abstract List<Role> groupRepresentationsToRoles(List<GroupRepresentation> groupRepresentation);
}
