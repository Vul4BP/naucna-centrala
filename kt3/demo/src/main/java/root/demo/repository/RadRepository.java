package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.demo.model.Rad;

public interface RadRepository extends JpaRepository<Rad, Long> {
    Rad findOneByNaslov(String naslov);
    Rad getById(Long id);
}
