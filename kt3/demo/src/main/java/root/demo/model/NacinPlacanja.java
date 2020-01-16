package root.demo.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class NacinPlacanja {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can't be blank")
    @Column(name="name")
    String name;
}
