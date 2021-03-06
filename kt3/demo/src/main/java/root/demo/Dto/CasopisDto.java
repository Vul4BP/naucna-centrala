package root.demo.Dto;

import lombok.Data;
import root.demo.model.NacinPlacanja;
import root.demo.model.NaucnaOblast;
import root.demo.model.Rad;

import java.io.Serializable;
import java.util.List;

@Data
public class CasopisDto {
    private Long id;
    private String naziv;
    private String issn;
    private Long clanarina;
    private String komeSePlaca;
    private boolean enabled;
    private List<NaucnaOblast> naucneoblasti;
    private List<UserDto> urednici;
    private List<UserDto> recenzenti;
    private List<NacinPlacanja> naciniPlacanja;
    private List<Rad> radovi;
}
