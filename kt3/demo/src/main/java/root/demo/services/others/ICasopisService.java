package root.demo.services.others;

import root.demo.Dto.CasopisDto;
import root.demo.model.Casopis;

import java.util.List;

public interface ICasopisService {
    public boolean validateCasopis(CasopisDto casopisDto);
    public abstract CasopisDto addCasopis(CasopisDto casopisDto, String username);
    public CasopisDto changeStatus(CasopisDto casopisDto);
    public boolean validateUredniciIRecenzenti(List<String> urednici, List<String> recenzenti);
    public abstract CasopisDto addUredniciIRecenzenti(String naziv, List<String> urednici, List<String> recenzenti);
    public CasopisDto getByNaziv(String naziv);
    public boolean validateEditovanjeCasopis(CasopisDto casopisDto);
    public abstract CasopisDto editCasopis(CasopisDto casopisDto);
    public List<CasopisDto> getAllMagazines();
    public Casopis getById(Long id);
}
