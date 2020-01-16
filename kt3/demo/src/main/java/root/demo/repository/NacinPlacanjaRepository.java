package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.demo.model.NacinPlacanja;
import java.util.List;

public interface NacinPlacanjaRepository extends JpaRepository<NacinPlacanja, Long> {

    NacinPlacanja findOneByName(String name);
    List<NacinPlacanja> findAll();
}

