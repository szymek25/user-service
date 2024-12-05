package pl.szymanski.user.service.controllers;

import io.swagger.client.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.dto.UserDTO;
import pl.szymanski.user.service.exception.DuplicatedUserException;
import pl.szymanski.user.service.facade.UserFacade;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
@Tag(name = "Accounts operations")
public class UserController {

	@Resource(name = "userFacadeWithKeycloakUserCalls")
	public UserFacade userFacade;


	@GetMapping
	@Operation(summary = "Retrieves all users list")
	@ApiResponse(responseCode = "200", description = "List of all users in system", content = @Content(schema = @Schema(implementation = UserDTO.class)))
	public @ResponseBody ResponseEntity<Page<UserDTO>> users(@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "50") int pageSize) {
		Page<UserDTO> users = userFacade.findAllUsers(currentPage, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@GetMapping("/customers")
	@Operation(summary = "Retrieves library customers list")
	@ApiResponse(responseCode = "200", description = "List of customers", content = @Content(schema = @Schema(implementation = UserDTO.class)))
	public @ResponseBody ResponseEntity<Page<UserDTO>> customers(@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "50") int pageSize) {
		Page<UserDTO> customers = userFacade.findCustomers(currentPage, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(customers);
	}

	@GetMapping("/employees")
	@Operation(summary = "Retrieves library employees list")
	@ApiResponse(responseCode = "200", description = "List of employees", content = @Content(schema = @Schema(implementation = UserDTO.class)))
	public @ResponseBody ResponseEntity<Page<UserDTO>> employees(@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "50") int pageSize) {
		Page<UserDTO> employees = userFacade.findEmployees(currentPage, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(employees);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by id")
	@ApiResponse(responseCode = "200", description = "Get user by id", content = @Content(schema = @Schema(implementation = UserDTO.class)))
	@ApiResponse(responseCode = "404", description = "User not found")
	public @ResponseBody ResponseEntity<UserDTO> getUserById(@PathVariable("id") int id) {
		UserDTO user = userFacade.findUserById(id);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@GetMapping("/getByEmail")
	@Operation(summary = "Get user by email")
	@ApiResponse(responseCode = "200", description = "Get user by email", content = @Content(schema = @Schema(implementation = UserDTO.class)))
	@ApiResponse(responseCode = "404", description = "User not found")
	public @ResponseBody ResponseEntity<UserDTO> getUserByEmail(@RequestParam("email") String id) {
		UserDTO user = userFacade.findUserByEmail(id);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@PostMapping("/add")
	@Operation(summary = "Add new user, operation typically should be involved by library employee")
	@ApiResponse(responseCode = "201", description = "user created")
	@ApiResponse(responseCode = "409", description = "User already exists")
	@ApiResponse(responseCode = "503", description = "Problem with keycloak service")
	public @ResponseBody ResponseEntity<String> addUser(@Valid @RequestBody AddUserDTO addUserDTO) {
		try {
			userFacade.addUser(addUserDTO);
		} catch (DuplicatedUserException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
		} catch (ApiException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getResponseBody());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("OK");
	}
}
