package root.demo.Dto;

import lombok.*;
import root.demo.model.Authority;
import root.demo.model.NaucnaOblast;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String password;
    private String ime;
    private String prezime;
    private String grad;
    private String drzava;
    private String email;
    private String titula;
    private String token;
    private List<NaucnaOblast> naucneoblasti;
    private List<Authority> authorities;
    private int expiresIn;
}

