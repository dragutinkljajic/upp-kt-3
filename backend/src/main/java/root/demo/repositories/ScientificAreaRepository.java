package root.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.ScientificArea;
import root.demo.model.User;

@Repository
public interface ScientificAreaRepository extends CrudRepository<ScientificArea, Long>{
	public ScientificArea save(ScientificArea scientificArea);
}