package pl.szymanski.user.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szymanski.user.service.dao.InitStateDao;
import pl.szymanski.user.service.model.InitState;
import pl.szymanski.user.service.service.InitStateService;

@Service
public class InitStateServiceImpl implements InitStateService {

	@Autowired private InitStateDao initStateDao;

	@Override
	public InitState getLatestInitState() {
		return initStateDao.findTop1ByOrderByLastUpdateDesc();
	}

	@Override
	public void save(InitState initState) {
		initStateDao.save(initState);
	}
}
