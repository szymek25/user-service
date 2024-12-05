package pl.szymanski.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;
import pl.szymanski.user.service.model.User;

@Mapper(componentModel = "spring")
public abstract class UserUpdateUserEventMapper {

	@Autowired
	protected UserMapperHelper helper;
	@Mapping(target = "keycloakId", expression = "java(helper.mapKeycloakId(event.getId()))")
	@Mapping(target = "role", expression = "java(helper.mapRole(event))")
	@Mapping(target = "dayOfBirth", expression = "java(java.sql.Date.valueOf(event.getDayOfBirth()))")
	public abstract User map(UpdateUserEvent event);
}
