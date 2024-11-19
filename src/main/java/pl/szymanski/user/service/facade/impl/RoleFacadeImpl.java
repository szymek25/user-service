package pl.szymanski.user.service.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.dto.RoleDTO;
import pl.szymanski.user.service.facade.RoleFacade;
import pl.szymanski.user.service.mapper.RoleRoleDTOMapper;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.service.RoleService;

import java.util.List;

@Component
public class RoleFacadeImpl implements RoleFacade {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleRoleDTOMapper roleRoleDTOMapper;

	@Override
	public List<RoleDTO> getAllRoles() {
		List<Role> roles = roleService.getAll();
		return roleRoleDTOMapper.rolesToRolesDTO(roles);
	}
}
