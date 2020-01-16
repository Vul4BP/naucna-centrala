package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.demo.model.Casopis;

@Repository
public interface CasopisRepository extends JpaRepository<Casopis, Long> {
    Casopis findByNaziv(String naziv);
    Casopis findByIssn(String issn);
}