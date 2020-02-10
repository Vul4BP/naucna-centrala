package root.demo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Rad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Naslov can't be null")
    @NotBlank(message = "Naslov can't be blank")
    @Size(min = 1, max = 50, message = "Naslov must be between 1 and 50 characters")
    @Column(name = "naslov", unique = true, nullable = false)
    private String naslov;

    @NotNull(message = "Apstrakt can't be null")
    @NotBlank(message = "Apstrakt can't be blank")
    @Size(min = 1, max = 50, message = "Apstrakt must be between 1 and 50 characters")
    @Column(name = "apstrakt", unique = false, nullable = false)
    private String apstrakt;

    @NotNull(message = "Kljucni pojmovi can't be null")
    @NotBlank(message = "Kljucni pojmovi can't be blank")
    @Size(min = 1, max = 100, message = "Kljucni pojmovi must be between 1 and 100 characters")
    @Column(name = "kljucni_pojmovi", unique = false, nullable = false)
    private String kljucniPojmovi;

    @NotNull(message = "Naucna oblast can't be null")
    @NotBlank(message = "Naucna oblast can't be blank")
    @Size(min = 1, max = 100, message = "Naucna oblast must be between 1 and 100 characters")
    @Column(name = "naucna_oblast", unique = false, nullable = false)
    private String naucnaOblast;

    @NotNull(message = "Pdf can't be null")
    @NotBlank(message = "Pdf can't be blank")
    @Size(min = 1, max = 100, message = "Pdf must be between 1 and 100 characters")
    @Column(name = "pdf", unique = false, nullable = true)
    private String pdf;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "rad_koautori",
            joinColumns = @JoinColumn(name = "rad_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "koautor_id", referencedColumnName = "id"))
    @NotNull(message = "Koautori can't be null")
    @Size(min = 1, message = "At least one Koautor is needed")
    private List<KoAutor> koautori;
}
