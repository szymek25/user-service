package pl.szymanski.user.service.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.szymanski.user.service.model.User;

import java.util.List;

public interface UserDao extends CrudRepository<User, String> {

	@Query("SELECT u FROM User u WHERE u.id IN :ids")
	List<User> findAllById(List<String> ids);
}
