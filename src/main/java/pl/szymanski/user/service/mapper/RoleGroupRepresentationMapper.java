package pl.szymanski.user.service.mapper;

import io.swagger.client.model.GroupRepresentation;
import org.mapstruct.Mapper;
import pl.szymanski.user.service.model.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleGroupRepresentationMapper {

	List<Role> groupRepresentationsToRoles(List<GroupRepresentation> groupRepresentation);
}
