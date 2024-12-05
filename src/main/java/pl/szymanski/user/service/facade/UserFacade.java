package pl.szymanski.user.service.facade;

import io.swagger.client.ApiException;
import org.springframework.data.domain.Page;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.exception.DuplicatedUserException;

public interface UserFacade {

	Page<UserDTO> findCustomers(int currentPage, int pageSize);

	Page<UserDTO> findEmployees(int currentPage, int pageSize);

	Page<UserDTO> findAllUsers(int currentPage, int pageSize);

	UserDTO findUserById(int id);

	UserDTO findUserByEmail(String email);

	boolean addUser(AddUserDTO userDTO) throws DuplicatedUserException, ApiException;

	boolean registerUser(RegisterUserDTO userDTO) throws DuplicatedUserException, ApiException;

}
