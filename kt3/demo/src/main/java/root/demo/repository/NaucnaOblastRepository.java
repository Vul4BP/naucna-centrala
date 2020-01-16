package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.demo.model.NaucnaOblast;

import java.util.List;

public interface NaucnaOblastRepository extends JpaRepository<NaucnaOblast, Long> {

    NaucnaOblast findOneByName(String name);
    List<NaucnaOblast> findAll();
}
