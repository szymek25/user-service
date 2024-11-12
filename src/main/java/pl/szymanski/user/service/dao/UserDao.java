package pl.szymanski.user.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;

import java.util.List;

public interface UserDao extends CrudRepository<User, String> {

	@Query("SELECT u FROM User u WHERE u.keycloakId IN :ids")
	List<User> findAllByKeycloakId(List<String> ids);

	Page<User> findByRole(Pageable pageable, Role role);

	User findByKeycloakId(String id);

	void deleteByKeycloakId(String keycloakId);

}
