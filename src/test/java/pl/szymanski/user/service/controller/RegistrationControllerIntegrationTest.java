package pl.szymanski.user.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.RegisterUserDTO;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegistrationControllerIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean(name = "keycloakUserServiceForTechnicalCalls")
	private KeycloakUserService keycloakUserService;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturn409WhenUserAlreadyExists() throws Exception {
		final RegisterUserDTO dto = createRegisterUserDTO("admin@biblioteka.com");
		this.mockMvc.perform(post("/register").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict())
				.andExpect(content().string("User already exists"));
	}

	@Test
	public void shouldReturn503InCaseOfIssuesInKeycloakWhenCreatingUser() throws Exception {
		final RegisterUserDTO dto = createRegisterUserDTO("test@test.com");

		when(keycloakUserService.createUser(any())).thenThrow(new ApiException("Keycloak is down"));
		this.mockMvc.perform(post("/register").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isServiceUnavailable());
	}

	@Test
	public void shouldReturnEmptyValidationMessagesWhenCreatingUser() throws Exception {
		final RegisterUserDTO dto = new RegisterUserDTO();
		this.mockMvc.perform(post("/register").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").value(ApplicationConstants.ValidationMessages.EMAIL_EMPTY))
				.andExpect(jsonPath("$.name").value(ApplicationConstants.ValidationMessages.NAME_EMPTY))
				.andExpect(jsonPath("$.lastName").value(ApplicationConstants.ValidationMessages.LAST_NAME_EMPTY))
				.andExpect(jsonPath("$.dayOfBirth").value(ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_EMPTY))
				.andExpect(jsonPath("$.phone").value(ApplicationConstants.ValidationMessages.PHONE_EMPTY))
				.andExpect(jsonPath("$.addressLine1").value(ApplicationConstants.ValidationMessages.ADDRESS_LINE_1_EMPTY))
				.andExpect(jsonPath("$.town").value(ApplicationConstants.ValidationMessages.TOWN_EMPTY))
				.andExpect(jsonPath("$.postalCode").value(ApplicationConstants.ValidationMessages.POSTAL_CODE_EMPTY))
				.andExpect(jsonPath("$.password").value(ApplicationConstants.ValidationMessages.PASSWORD_EMPTY));
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnMessagesForInvalidValuesWhenCreatingUser() throws Exception {
		final RegisterUserDTO dto = createRegisterUserDTO("invalid");
		dto.setPhone("ssss");
		dto.setDayOfBirth("12-12-2002");

		this.mockMvc.perform(post("/register").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").value(ApplicationConstants.ValidationMessages.EMAIL_INVALID))
				.andExpect(jsonPath("$.phone").value(ApplicationConstants.ValidationMessages.PHONE_INVALID))
				.andExpect(jsonPath("$.dayOfBirth").value(ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_INVALID));

	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldAddNewUser() throws Exception {
		final RegisterUserDTO dto = createRegisterUserDTO("test1@test.com");

		when(keycloakUserService.createUser(any())).thenReturn(true);

		this.mockMvc.perform(post("/register").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(content().string("OK"));
	}


	private static RegisterUserDTO createRegisterUserDTO(String email) {
		final RegisterUserDTO dto = new RegisterUserDTO();
		dto.setEmail(email);
		dto.setTown("test");
		dto.setPostalCode("test");
		dto.setPhone("1111");
		dto.setDayOfBirth("1990-11-11");
		dto.setAddressLine1("test");
		dto.setLastName("test");
		dto.setName("test");
		dto.setPassword("test");

		return dto;
	}
}
