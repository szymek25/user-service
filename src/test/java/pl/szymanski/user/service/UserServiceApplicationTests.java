package pl.szymanski.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.metrics.ApplicationStartup;

@SpringBootTest
@ComponentScan(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		value = { ApplicationStartup.class}))
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
