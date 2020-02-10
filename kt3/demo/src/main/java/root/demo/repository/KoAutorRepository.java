package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.demo.model.KoAutor;
import root.demo.model.Rad;

public interface KoAutorRepository extends JpaRepository<KoAutor, Long> {
    KoAutor getByImeAndEmail(String ime, String email);
    KoAutor getById(Long id);
}

