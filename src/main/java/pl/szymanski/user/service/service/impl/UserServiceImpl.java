package pl.szymanski.user.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dao.UserDao;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.RoleService;
import pl.szymanski.user.service.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleService roleService;

	@Override
	public void saveAll(List<User> users) {
		userDao.saveAll(users);
	}

	@Override
	public void deleteAll() {
		userDao.deleteAll();
	}

	@Override
	public List<User> findAllByKeycloakId(List<String> ids) {
		return userDao.findAllByKeycloakId(ids);
	}

	@Override
	public Page<User> findCustomers(Pageable pageable) {
		final Role userRole = roleService.getByName(ApplicationConstants.USER_ROLE_NAME);
		return userDao.findByRole(pageable, userRole);

	}
}
