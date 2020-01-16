package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class KreiranjeCasopisaService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("KREIRANJE CASOPISA SERVICE");
        System.out.println("KREIRANJE CASOPISA USPESNO IZVRSENO");
    }
}