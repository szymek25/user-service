package pl.szymanski.user.service.mapper;

import io.swagger.client.model.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;

@Mapper(componentModel = "spring")
public abstract class RegisterUserDtoUserRepresentationMapper {

	@Autowired
	protected UserMapperHelper helper;
	@Mapping(target = "groups", expression = "java(helper.mapUserRoleGroup())")
	@Mapping(target = "firstName", source = "registerUserDTO.name")
	@Mapping(target = "attributes", expression = "java(helper.mapAttributes(registerUserDTO))")
	@Mapping(target = "username", source = "registerUserDTO.email")
	@Mapping(target = "enabled", constant = "true")
	@Mapping(target = "emailVerified", constant = "true")
	@Mapping(target = "credentials", expression = "java(helper.mapCredentials(registerUserDTO.getPassword()))")
	public abstract UserRepresentation map(RegisterUserDTO registerUserDTO);
}
