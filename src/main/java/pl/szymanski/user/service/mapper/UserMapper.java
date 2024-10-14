package pl.szymanski.user.service.mapper;

import io.swagger.client.model.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;
import pl.szymanski.user.service.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

	@Autowired
	protected UserMapperHelper helper;

	@Mapping(target = "name", source = "userRepresentation.firstName")
	@Mapping(target = "phone", expression = "java(helper.mapStringAttribute(userRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.PHONE))")
	@Mapping(target = "addressLine1", expression = "java(helper.mapStringAttribute(userRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.ADDRESS_LINE_1))")
	@Mapping(target = "town", expression = "java(helper.mapStringAttribute(userRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.TOWN))")
	@Mapping(target = "postalCode", expression = "java(helper.mapStringAttribute(userRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.POSTAL_CODE))")
	@Mapping(target = "dayOfBirth", expression = "java(helper.mapBirthDate(userRepresentation))")
	@Mapping(target = "id", expression = "java(helper.mapStringAttribute(userRepresentation, pl.szymanski.user.service.constants.ApplicationConstants.KeyCloak.UUID))")
	public abstract User map(UserRepresentation userRepresentation);
}
