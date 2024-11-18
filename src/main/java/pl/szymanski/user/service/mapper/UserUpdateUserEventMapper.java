package pl.szymanski.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.model.User;

@Mapper(componentModel = "spring")
public interface UserUpdateUserEventMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "keycloakId", source = "id")
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "dayOfBirth", expression = "java(java.sql.Date.valueOf(event.getDayOfBirth()))")
	User map(UpdateUserEvent event);
}
