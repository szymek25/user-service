package pl.szymanski.user.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

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
}
