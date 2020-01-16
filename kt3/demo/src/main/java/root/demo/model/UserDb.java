package root.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserDb implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Username can't be null")
    @NotBlank(message = "Username can't be blank")
    @Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotNull(message = "Password can't be null")
    @NotBlank(message = "Password can't be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 20 characters")
    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @NotNull(message = "Ime can't be null")
    @NotBlank(message = "Ime can't be blank")
    @Size(min = 2, max = 50, message = "Ime must be between 2 and 50 characters")
    @Column(name = "ime", unique = false, nullable = false)
    private String ime;

    @NotNull(message = "Prezime can't be null")
    @NotBlank(message = "Prezime can't be blank")
    @Size(min = 2, max = 50, message = "Prezime must be between 2 and 50 characters")
    @Column(name = "prezime", unique = false, nullable = false)
    private String prezime;

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

    @NotNull(message = "Email can't be null")
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /*
    @NotNull(message = "Oblasti can't be null")
    @NotBlank(message = "Oblasti can't be blank")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    @Column(name = "oblasti", unique = false, nullable = false)
    private String oblasti;*/

    @Column(name = "titula", unique = false, nullable = true)
    private String titula;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    @ManyToMany()
    @JoinTable(name = "user_naucnaoblast",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "oblast_id", referencedColumnName = "id"))
    @NotNull(message = "NaucneOblasti can't be null")
    @Size(min = 1, message = "At least one NaucnaOblast is needed")
    private List<NaucnaOblast> naucneoblasti;

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
    @JsonIgnore
    @ManyToMany(mappedBy = "urednici")
    List<Casopis> casopisiUrednik;

    @JsonIgnore
    @ManyToMany(mappedBy = "recenzenti")
    List<Casopis> casopisRecenzent;*/
}
