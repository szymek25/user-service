package pl.szymanski.user.service.dao;


import org.springframework.data.repository.CrudRepository;
import pl.szymanski.user.service.model.InitState;

public interface InitStateDao extends CrudRepository<InitState, Integer> {

	InitState findTop1ByOrderByLastUpdateDesc();
}
