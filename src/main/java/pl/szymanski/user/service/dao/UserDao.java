package pl.szymanski.user.service.dao;

import org.springframework.data.repository.CrudRepository;
import pl.szymanski.user.service.model.User;

public interface UserDao extends CrudRepository<User, String> {
}
