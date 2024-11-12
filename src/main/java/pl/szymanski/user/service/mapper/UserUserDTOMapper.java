package pl.szymanski.user.service.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.model.User;

@Mapper(componentModel = "spring")
public interface UserUserDTOMapper {

	UserDTO mapToUserDTO(User user);
}
