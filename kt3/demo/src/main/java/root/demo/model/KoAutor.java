package root.demo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class KoAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Ime can't be null")
    @NotBlank(message = "Ime can't be blank")
    @Size(min = 2, max = 50, message = "Ime must be between 2 and 50 characters")
    @Column(name = "ime", unique = false, nullable = false)
    private String ime;

    @NotNull(message = "Email can't be null")
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email is not valid")
    @Column(name = "email", unique = false, nullable = false)
    private String email;

    @NotNull(message = "Grad can't be null")
    @NotBlank(message = "Grad can't be blank")
    @Size(min = 2, max = 50, message = "Grad must be between 2 and 50 characters")
    @Column(name = "grad", unique = false, nullable = false)
    private String grad;

    @NotNull(message = "Drzava can't be null")
    @NotBlank(message = "Drzava can't be blank")
    @Size(min = 2, max = 50, message = "Drzava must be between 2 and 50 characters")
    @Column(name = "drzava", unique = false, nullable = false)
    private String drzava;

    @Column(name = "userId", unique = false, nullable = true)
    private Long userId;


}
