package pl.szymanski.user.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.keycloak.api.KeycloakUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean(name = "keycloakUserServiceForApiCalls")
	private KeycloakUserService keycloakUserService;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnListOfLibraryCustomers() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/users/customers"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("page.totalElements").value(5))
				.andExpect(jsonPath("page.size").value(50))
				.andExpect(jsonPath("content").hasJsonPath())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content[0].id").hasJsonPath())
				.andReturn();

	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnListOfLibraryEmployees() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/users/employees"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("page.totalElements").value(3))
				.andExpect(jsonPath("page.size").value(50))
				.andExpect(jsonPath("content").hasJsonPath())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content[0].email").value("admin@biblioteka.com"))
				.andExpect(jsonPath("content[1].email").value("jan@biblioteka.com"))
				.andExpect(jsonPath("content[2].email").value("andrzej@biblioteka.com"))
				.andReturn();

	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnAllUsers() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("page.totalElements").value(9))
				.andExpect(jsonPath("page.size").value(50))
				.andExpect(jsonPath("content").hasJsonPath())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content[0].email").value("admin@biblioteka.com"))
				.andExpect(jsonPath("content[0].role.id").value("13fbb108-1b6e-46f5-9e0d-d78f4bca1efc"))
				.andExpect(jsonPath("content[0].role.name").value("ROLE_EMPLOYEE"))
				.andExpect(jsonPath("content[0].role.description").value("Pracownik"))
				.andReturn();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnUserById() throws Exception {
		final String id = "aa183f01-9487-437e-9d40-6665286fd641";
		MvcResult mvcResult = this.mockMvc.perform(get("/users/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("keycloakId").value(id))
				.andExpect(jsonPath("email").value("admin@biblioteka.com"))
				.andReturn();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturn404WhenUserNotFound() throws Exception {
		final String id = "ssss-aaaa";
		this.mockMvc.perform(get("/users/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnUserByEmail() throws Exception {
		final String email = "admin@biblioteka.com";
		MvcResult mvcResult = this.mockMvc.perform(get("/users/getByEmail").param("email", email))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("email").value("admin@biblioteka.com"))
				.andReturn();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturn404WhenUserNotFoundByEmail() throws Exception {
		final String email = "ssss-aaaa";
		this.mockMvc.perform(get("/users/getByEmail").param("email", email))
				.andExpect(status().isNotFound());
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturn409WhenUserAlreadyExists() throws Exception {
		final AddUserDTO dto = createAddUserDTO("admin@biblioteka.com");
		this.mockMvc.perform(post("/users/add").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict())
				.andExpect(content().string("User already exists"));
	}

	@Test
	public void shouldReturn503InCaseOfIssuesInKeycloakWhenCreatingUser() throws Exception {
		final AddUserDTO dto = createAddUserDTO("test@test.com");

		when(keycloakUserService.createUser(any())).thenThrow(new ApiException("Keycloak is down"));
		this.mockMvc.perform(post("/users/add").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isServiceUnavailable());
	}

	@Test
	public void shouldReturnEmptyValidationMessagesWhenCreatingUser() throws Exception {
		final AddUserDTO dto = new AddUserDTO();
		this.mockMvc.perform(post("/users/add").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").value(ApplicationConstants.ValidationMessages.EMAIL_EMPTY))
				.andExpect(jsonPath("$.name").value(ApplicationConstants.ValidationMessages.NAME_EMPTY))
				.andExpect(jsonPath("$.lastName").value(ApplicationConstants.ValidationMessages.LAST_NAME_EMPTY))
				.andExpect(jsonPath("$.dayOfBirth").value(ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_EMPTY))
				.andExpect(jsonPath("$.phone").value(ApplicationConstants.ValidationMessages.PHONE_EMPTY))
				.andExpect(jsonPath("$.addressLine1").value(ApplicationConstants.ValidationMessages.ADDRESS_LINE_1_EMPTY))
				.andExpect(jsonPath("$.town").value(ApplicationConstants.ValidationMessages.TOWN_EMPTY))
				.andExpect(jsonPath("$.postalCode").value(ApplicationConstants.ValidationMessages.POSTAL_CODE_EMPTY))
				.andExpect(jsonPath("$.roleId").value(ApplicationConstants.ValidationMessages.ROLE_EMPTY))
				.andExpect(jsonPath("$.password").value(ApplicationConstants.ValidationMessages.PASSWORD_EMPTY));
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnMessagesForInvalidValuesWhenCreatingUser() throws Exception {
		final AddUserDTO dto = createAddUserDTO("invalid");
		dto.setRoleId("2222");
		dto.setPhone("ssss");
		dto.setDayOfBirth("12-12-2002");

		this.mockMvc.perform(post("/users/add").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.roleId").value(ApplicationConstants.ValidationMessages.INVALID_ROLE))
				.andExpect(jsonPath("$.email").value(ApplicationConstants.ValidationMessages.EMAIL_INVALID))
				.andExpect(jsonPath("$.phone").value(ApplicationConstants.ValidationMessages.PHONE_INVALID))
				.andExpect(jsonPath("$.dayOfBirth").value(ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_INVALID));

	}

	private static AddUserDTO createAddUserDTO(String email) {
		final AddUserDTO dto = new AddUserDTO();
		dto.setEmail(email);
		dto.setTown("test");
		dto.setPostalCode("test");
		dto.setPhone("1111");
		dto.setRoleId("13fbb108-1b6e-46f5-9e0d-d78f4bca1efc");
		dto.setDayOfBirth("1990-11-11");
		dto.setAddressLine1("test");
		dto.setLastName("test");
		dto.setName("test");
		dto.setPassword("test");

		return dto;
	}
}
