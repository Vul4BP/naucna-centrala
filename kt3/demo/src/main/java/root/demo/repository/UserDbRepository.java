package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.demo.model.UserDb;

@Repository
public interface UserDbRepository extends JpaRepository<UserDb, Long> {
    UserDb findByUsername(String username);
    UserDb findByEmail(String email);
    UserDb findByImeAndEmail(String ime, String email);
}
