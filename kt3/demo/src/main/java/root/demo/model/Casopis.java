package root.demo.model;

import com.sun.org.apache.xerces.internal.impl.XMLEntityScanner;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Casopis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Naziv can't be null")
    @NotBlank(message = "Naziv can't be blank")
    @Column(name="naziv")
    private String naziv;

    @NotNull(message = "Issn can't be null")
    @NotBlank(message = "Issn can't be blank")
    @Column(name="issn")
    @Size(min = 8, max = 8, message = "ISSN must be 8 characters long")
    private String issn;

    @NotNull(message = "Clanarina can't be null")
    @Min(value = 0, message = "Clanarina must be >= 0")
    @Column(name="clanarina")
    private Long clanarina;

    @NotNull(message = "KomeSePlaca can't be null")
    @NotBlank(message = "KomeSePlaca can't be blank")
    @Column(name="komeseplaca")
    private String komeSePlaca;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany()
    @JoinTable(name = "casopis_urednik",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "urednik_id", referencedColumnName = "id"))
    private List<UserDb> urednici;

    @ManyToMany()
    @JoinTable(name = "casopis_recenzent",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id", referencedColumnName = "id"))
    private List<UserDb> recenzenti;

    @ManyToMany()
    @JoinTable(name = "casopis_naucnaoblast",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucnaoblast_id", referencedColumnName = "id"))
    @NotNull(message = "NaucneOblasti can't be null")
    @Size(min = 1, message = "At least one NaucnaOblast is needed")
    private List<NaucnaOblast> naucneoblasti;

    @ManyToMany()
    @JoinTable(name = "casopis_placanje",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "placanje_id", referencedColumnName = "id"))
    @NotNull(message = "NacinPlacanja can't be null")
    @Size(min = 1, message = "At least one NacinPlacanja is needed")
    private List<NacinPlacanja> naciniplacanja;
}
