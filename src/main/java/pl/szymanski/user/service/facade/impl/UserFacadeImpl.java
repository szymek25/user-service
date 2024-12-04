package pl.szymanski.user.service.facade.impl;

import io.swagger.client.ApiException;
import io.swagger.client.model.UserRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.exception.DuplicatedUserException;
import pl.szymanski.user.service.facade.UserFacade;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;
import pl.szymanski.user.service.mapper.AddUserDtoUserRepresentationMapper;
import pl.szymanski.user.service.mapper.RegisterUserDtoUserRepresentationMapper;
import pl.szymanski.user.service.mapper.UserUserDTOMapper;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.UserService;

@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

	@Autowired
	private UserService userService;

	@Autowired
	private UserUserDTOMapper userUserDTOMapper;

	@NonNull
	private KeycloakUserService keycloakUserService;

	@Autowired
	private AddUserDtoUserRepresentationMapper addUserDtoUserRepresentationMapper;

	@Autowired
	private RegisterUserDtoUserRepresentationMapper registerUserDtoUserRepresentationMapper;

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

	@Override
	public UserDTO findUserById(String id) {
		final User user = userService.findByKeycloakId(id);
		return userUserDTOMapper.mapToUserDTO(user);
	}

	@Override
	public UserDTO findUserByEmail(String email) {
		User user = userService.findByEmail(email);
		return userUserDTOMapper.mapToUserDTO(user);
	}

	@Override
	public boolean addUser(AddUserDTO userDTO) throws DuplicatedUserException, ApiException {
		checkIfUserExists(userDTO.getEmail());
		UserRepresentation userRepresentation = addUserDtoUserRepresentationMapper.map(userDTO);
		return keycloakUserService.createUser(userRepresentation);
	}

	@Override
	public boolean registerUser(RegisterUserDTO userDTO) throws DuplicatedUserException, ApiException {
		checkIfUserExists(userDTO.getEmail());
		UserRepresentation map = registerUserDtoUserRepresentationMapper.map(userDTO);
		return keycloakUserService.createUser(map);
	}

	private void checkIfUserExists(String userDTO) {
		final User byEmail = userService.findByEmail(userDTO);
		if (byEmail != null) {
			throw new DuplicatedUserException("User with email: " + userDTO + " already exists");
		}
	}
}
