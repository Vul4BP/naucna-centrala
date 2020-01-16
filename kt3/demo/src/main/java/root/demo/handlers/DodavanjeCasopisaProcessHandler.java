package root.demo.handlers;


import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.services.others.UserDbService;

@Service
public class DodavanjeCasopisaProcessHandler implements ExecutionListener {
    @Autowired
    IdentityService identityService;

    @Autowired
    private UserDbService userDbService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("DODAVANJE CASOPISA PROCESS HANDLER");
        execution.setVariable("validacija1", false);
        System.out.println("--> postavljena variabla validacija1");
        execution.setVariable("validacija2", false);
        System.out.println("--> postavljena variabla validacija2");
        execution.setVariable("aktivan", false);
        System.out.println("--> postavljena variabla aktivan");
    }
}

