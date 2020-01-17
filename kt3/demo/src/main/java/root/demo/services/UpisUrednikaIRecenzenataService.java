package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UpisUrednikaIRecenzenataService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("UPIS URENDIKA I RECENZENATA U CASOPIS SERVICE");
        System.out.println("UPIS URENDIKA I RECENZENATA U CASOPIS USPESNO IZVRSENO");
    }
}
