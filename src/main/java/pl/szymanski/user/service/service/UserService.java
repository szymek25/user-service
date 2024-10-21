package pl.szymanski.user.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.user.service.model.User;

import java.util.List;

public interface UserService {

	void saveAll(List<User> users);
	void deleteAll();
	List<User> findAllByKeycloakId(List<String> ids);

	Page<User> findCustomers(Pageable pageable);
}
