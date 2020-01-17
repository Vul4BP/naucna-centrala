package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class OsveziValidacijeCasopisaService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("OSVEZAVANJE VALIDACIJA CASOPISA SERVICE");
        execution.setVariable("validacija1",false);
        execution.setVariable("validacija2",false);
        execution.setVariable("editovanje",true);
    }
}
