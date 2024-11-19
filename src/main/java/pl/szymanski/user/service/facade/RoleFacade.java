package pl.szymanski.user.service.facade;

import pl.szymanski.user.service.dto.RoleDTO;

import java.util.List;

public interface RoleFacade {

	List<RoleDTO> getAllRoles();
}
