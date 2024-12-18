package pl.szymanski.user.service.dao;

import org.springframework.data.repository.CrudRepository;
import pl.szymanski.user.service.model.Role;

import java.util.List;

public interface RoleDao extends CrudRepository<Role, Integer> {

	Role findByName(String name);

	Role findById(String id);

	List<Role> findAll();
}
