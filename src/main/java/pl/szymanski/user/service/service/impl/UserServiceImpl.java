package pl.szymanski.user.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szymanski.user.service.dao.UserDao;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public void saveAll(List<User> users) {
		userDao.saveAll(users);
	}

	@Override
	public void deleteAll() {
		userDao.deleteAll();
	}
}
