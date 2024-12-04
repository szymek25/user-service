package pl.szymanski.user.service.controllers;

import io.swagger.client.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.exception.DuplicatedUserException;
import pl.szymanski.user.service.facade.UserFacade;

import javax.annotation.Resource;

@RestController
@RequestMapping("/register")
@Tag(name = "Accounts operations")
public class RegistrationController {

	@Resource(name = "userFacadeWithKeycloakTechnicalCalls")
	private UserFacade userFacade;

	@PostMapping
	@Operation(summary = "Register new user with ROLE_USER role")
	@ApiResponse(responseCode = "201", description = "user created")
	@ApiResponse(responseCode = "409", description = "User already exists")
	@ApiResponse(responseCode = "503", description = "Problem with keycloak service")
	public @ResponseBody ResponseEntity<String> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
		try {
			userFacade.registerUser(registerUserDTO);
		} catch (DuplicatedUserException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
		} catch (ApiException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getResponseBody());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("OK");
	}
}
