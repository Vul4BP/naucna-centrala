package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.Casopis;
import root.demo.repository.CasopisRepository;
import root.demo.services.others.CasopisService;


@Service
public class AktiviranjeCasopisaService implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("AKTIVIRANJE CASOPISA SERVICE");
        String name = (String) execution.getVariable("naziv");
        Casopis casopis = casopisRepository.findByNaziv(name);
        if(casopis != null){
            if(casopisService.postujSelleru(casopis) != null){
                casopis.setEnabled(true);
                execution.setVariable("aktivan", true);
                System.out.println("STATUS CASOPISA JE PREBACEN U AKTIVAN");
                System.out.println("PROCES DODAVANJA CASOPISA JE ZAVRSEN");
            }
        }
    }
}
