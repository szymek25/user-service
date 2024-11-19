package pl.szymanski.user.service.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.user.service.dto.RoleDTO;
import pl.szymanski.user.service.model.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleRoleDTOMapper {

	RoleDTO roleToRoleDTO(Role role);

	List<RoleDTO> rolesToRolesDTO(List<Role> roles);

}
