package pl.szymanski.user.service.facade;

import org.springframework.data.domain.Page;
import pl.szymanski.user.service.dto.UserDTO;

public interface UserFacade {

	Page<UserDTO> findCustomers(int currentPage, int pageSize);
}
