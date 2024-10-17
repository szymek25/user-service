package pl.szymanski.user.service.service;

import pl.szymanski.user.service.model.User;

import java.util.List;

public interface UserService {

	void saveAll(List<User> users);
	void deleteAll();
	List<User> findAllById(List<String> ids);
}
