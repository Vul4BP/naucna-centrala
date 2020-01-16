package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.demo.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findOneByName(String name);

}
