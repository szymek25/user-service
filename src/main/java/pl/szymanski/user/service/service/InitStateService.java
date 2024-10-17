package pl.szymanski.user.service.service;

import pl.szymanski.user.service.model.InitState;

public interface InitStateService {

	InitState getLatestInitState();

	void save(InitState initState);
}
