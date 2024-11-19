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
public class RoleControllerIntegrationTest {


	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	@Sql(scripts = "/scripts/users.sql")
	public void shouldReturnAllRoles() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/roles"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("[0].id").value("13fbb108-1b6e-46f5-9e0d-d78f4bca1efc"))
				.andExpect(jsonPath("[0].name").value("ROLE_EMPLOYEE"))
				.andExpect(jsonPath("[0].description").value("Pracownik"))
				.andExpect(jsonPath("[1].id").value("f376a0e2-d26c-4c66-beb9-e5d5d63cd687"))
				.andExpect(jsonPath("[1].name").value("ROLE_USER"))
				.andExpect(jsonPath("[1].description").value("Użytkownik"))
				.andExpect(jsonPath("[2].id").value("ca704dd5-067d-4847-a9b2-06c91c4b8744"))
				.andExpect(jsonPath("[2].name").value("ROLE_MANAGER"))
				.andExpect(jsonPath("[2].description").value("Menadżer"))
				.andReturn();

	}
}
