package root.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	public User save(User user);
	public Optional<User> findById(Long id);
	public void deleteById(Long id);
	public Set<User> findAll();
	public User findByUsername(String username);
}