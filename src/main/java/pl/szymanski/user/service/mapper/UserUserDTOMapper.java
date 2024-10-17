package pl.szymanski.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.model.User;

@Mapper(componentModel = "spring")
public interface UserUserDTOMapper {

	@Mapping(target = "roleId", source = "role.id")
	UserDTO mapToUserDTO(User user);
}
