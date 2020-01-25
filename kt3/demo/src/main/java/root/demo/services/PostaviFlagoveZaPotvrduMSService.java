package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.Casopis;
import root.demo.model.NacinPlacanja;
import root.demo.repository.CasopisRepository;

@Service
public class PostaviFlagoveZaPotvrduMSService implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("POSTAVLJANJE FLAGOVA ZA POTVRDU MS SERVICE");
        String naziv = (String)execution.getVariable("naziv");
        Casopis casopis = casopisRepository.findByNaziv(naziv);
        for(NacinPlacanja nc : casopis.getNaciniplacanja()){
            execution.setVariable(nc.getName(),false);
        }
    }
}
