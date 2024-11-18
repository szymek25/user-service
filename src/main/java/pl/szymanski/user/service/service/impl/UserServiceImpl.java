package pl.szymanski.user.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dao.UserDao;
import pl.szymanski.user.service.exception.UserNotFoundException;
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

	@Override
	public User findByKeycloakId(String id) {
		return userDao.findByKeycloakId(id);
	}

	@Override
	public void save(User user) {
		userDao.save(user);
	}

	@Override
	public void delete(String keycloakId) {
		userDao.deleteByKeycloakId(keycloakId);
	}

	@Override
	public void assignRole(User user, Role role) {
		user.setRole(role);
		userDao.save(user);
	}

	@Override
	public Page<User> findEmployees(Pageable pageable) {
		final Role userRole = roleService.getByName(ApplicationConstants.ROLE_EMPLOYEE_NAME);
		return userDao.findByRole(pageable, userRole);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	public User update(final User user, final String keycloakId) {
		User existingUser = findByKeycloakId(keycloakId);
		if (existingUser == null) {
			throw new UserNotFoundException("User with keycloakId: " + keycloakId + " not found");
		} else {
			updateUser(existingUser, user);
			userDao.save(existingUser);
			return existingUser;
		}
	}

	private void updateUser(final User existingUser, final User user) {
		existingUser.setAddressLine1(user.getAddressLine1());
		existingUser.setDayOfBirth(user.getDayOfBirth());
		existingUser.setEmail(user.getEmail());
		existingUser.setLastName(user.getLastName());
		existingUser.setName(user.getName());
		existingUser.setPhone(user.getPhone());
		existingUser.setTown(user.getTown());
		existingUser.setPostalCode(user.getPostalCode());
	}

}
