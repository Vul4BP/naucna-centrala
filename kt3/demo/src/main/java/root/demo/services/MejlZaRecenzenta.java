package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.services.others.EmailService;

@Service
public class MejlZaRecenzenta implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJL RECENZENTU ZA RECENZIJU RADA SERVICE");
        String recenzent_email = (String) execution.getVariable("recenzentId");

        String message = "Hello!\n";
        message += "Article needs to be checked.\n";
        //emailService.sendMail(recenzent_email, "CHECK ARTICLE", message);

        System.out.println("--Poslato na " + recenzent_email);
    }
}
