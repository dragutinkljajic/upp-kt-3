package root.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.User;
import root.demo.model.Verification;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, Long>{
	public Verification save(Verification verification);
	public Verification findByCode(String code);
}
