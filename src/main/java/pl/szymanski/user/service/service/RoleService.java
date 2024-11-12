package pl.szymanski.user.service.service;

import pl.szymanski.user.service.model.Role;

import java.util.List;

public interface RoleService {

	void saveAll(List<Role> roleList);

	void deleteAll();

	Role getByName(String name);

	Role getById(String id);

}
