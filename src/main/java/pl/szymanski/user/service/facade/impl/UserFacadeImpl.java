package pl.szymanski.user.service.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.facade.UserFacade;
import pl.szymanski.user.service.mapper.UserUserDTOMapper;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

@Component
public class UserFacadeImpl implements UserFacade {

	@Autowired
	private UserService userService;

	@Autowired
	private UserUserDTOMapper userUserDTOMapper;

	@Override
	public Page<UserDTO> findCustomers(int currentPage, int pageSize) {
		final Pageable pageable = PageRequest.of(currentPage, pageSize);
		Page<User> customers = userService.findCustomers(pageable);
		return customers.map(userUserDTOMapper::mapToUserDTO);
	}

	@Override
	public Page<UserDTO> findEmployees(int currentPage, int pageSize) {
		final Pageable pageable = PageRequest.of(currentPage, pageSize);
		Page<User> employees = userService.findEmployees(pageable);
		return employees.map(userUserDTOMapper::mapToUserDTO);
	}

	@Override
	public Page<UserDTO> findAllUsers(int currentPage, int pageSize) {
		final Pageable pageable = PageRequest.of(currentPage, pageSize);
		Page<User> users = userService.findAll(pageable);
		return users.map(userUserDTOMapper::mapToUserDTO);
	}
}
