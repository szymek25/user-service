package pl.szymanski.user.service.mapper;

import io.swagger.client.model.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;
import pl.szymanski.user.service.model.User;

import java.util.List;

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
	@Mapping(target = "role", expression = "java(helper.mapRole(userRepresentation))")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "keycloakId", source = "userRepresentation.id")
	public abstract User map(UserRepresentation userRepresentation);

	public abstract List<User> map(List<UserRepresentation> userRepresentation);

	@Mapping(target = "firstName", source = "user.name")
	@Mapping(target = "id", source = "user.keycloakId")
	@Mapping(target = "username", source = "user.email")
	@Mapping(target = "attributes", expression = "java(helper.mapAttributes(user))")
	@Mapping(target = "groups", expression = "java(helper.mapRoles(user))")
	public abstract UserRepresentation mapToUserRepresentation(User user);

}
