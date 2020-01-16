package root.demo.services.others;

import root.demo.Dto.CasopisDto;

public interface ICasopisService {
    public boolean validateCasopis(CasopisDto casopisDto);
    public abstract CasopisDto addCasopis(CasopisDto casopisDto, String username);
    public abstract CasopisDto changeStatus(CasopisDto casopisDto);
}
