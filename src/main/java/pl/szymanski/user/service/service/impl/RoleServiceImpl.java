package pl.szymanski.user.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szymanski.user.service.dao.RoleDao;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Override
	public void saveAll(List<Role> roleList) {
		roleDao.saveAll(roleList);
	}

	@Override
	public void deleteAll() {
		roleDao.deleteAll();
	}

	@Override
	public Role getByName(String name) {
		return roleDao.findByName(name);
	}

	@Override
	public Role getById(String id) {
		return roleDao.findById(id);
	}

	@Override
	public List<Role> getAll() {
		return roleDao.findAll();
	}
}
