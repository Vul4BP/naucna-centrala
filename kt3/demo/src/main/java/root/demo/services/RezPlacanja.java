package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.Casopis;
import root.demo.model.UserDb;
import root.demo.repository.CasopisRepository;

import java.util.List;

@Service
public class RezPlacanja implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("REZ PLACANJA SERVICE");
        boolean aktivna_clanarina = (boolean)delegateExecution.getVariable("aktivna_clanarina");
        System.out.println("--Aktivna clanarina -> " + aktivna_clanarina);
    }
}
