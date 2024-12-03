package pl.szymanski.user.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;

import java.util.List;

public interface UserService {

	void saveAll(List<User> users);
	void deleteAll();
	List<User> findAllByKeycloakId(List<String> ids);

	Page<User> findCustomers(Pageable pageable);

	User findByKeycloakId(String id);

	void save(User user);

	void delete(String keycloakId);

	void assignRole(User user, Role role);

	Page<User> findEmployees(Pageable pageable);

	Page<User> findAll(Pageable pageable);

	User update(User user, String keycloakId);

	String getCurrentRoleOfUser(String keycloakId);

	User findByEmail(String email);


}
