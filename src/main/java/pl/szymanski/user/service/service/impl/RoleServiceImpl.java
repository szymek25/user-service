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
}