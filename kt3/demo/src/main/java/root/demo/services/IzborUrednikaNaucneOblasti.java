package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.CasopisDto;
import root.demo.Dto.UserDto;
import root.demo.model.Casopis;
import root.demo.model.NaucnaOblast;
import root.demo.services.others.CasopisService;
import root.demo.services.others.EmailService;
import root.demo.services.others.UserDbService;

import java.util.List;

@Service
public class IzborUrednikaNaucneOblasti implements JavaDelegate {

    @Autowired
    private CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("IZBOR UREDNIKA NAUCNE OBLASTI SERVICE");
        String imeCasopisa = (String) execution.getVariable("selImeCasopisa");
        String naucnaOblast = (String) execution.getVariable("naucna_oblast_rada");

        CasopisDto casopis = casopisService.getByNaziv(imeCasopisa);
        List<UserDto> urednici = casopis.getUrednici();
        for (Integer i = 1; i < urednici.size(); i++) {   //idemo od 1 jer je na nultom indeksu glavni urednik
            UserDto user = urednici.get(i);
            for (NaucnaOblast nc : user.getNaucneoblasti()) {
                if (nc.getName().equals(naucnaOblast)) {
                    execution.setVariable("urednik_za_odabranu_naucnu_oblast", user.getUsername());
                    return;
                }
            }
        }

        //ako nije pronadjen nijedan urendik, onda ovu ulogu dobija glavni urednik
        execution.setVariable("urednik_za_odabranu_naucnu_oblast", urednici.get(0).getUsername());
    }
}
