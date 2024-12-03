package pl.szymanski.user.service.mapper;

import io.swagger.client.model.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;

@Mapper(componentModel = "spring")
public abstract class AddUserDtoUserRepresentationMapper {

	@Autowired
	protected UserMapperHelper helper;
	@Mapping(target = "groups", expression = "java(helper.mapGroups(addUserDto.getRoleId()))")
	@Mapping(target = "firstName", source = "addUserDto.name")
	@Mapping(target = "attributes", expression = "java(helper.mapAttributes(addUserDto))")
	@Mapping(target = "username", source = "addUserDto.email")
	@Mapping(target = "enabled", constant = "true")
	@Mapping(target = "emailVerified", constant = "true")
	public abstract UserRepresentation map(AddUserDTO addUserDto);
}
