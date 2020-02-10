package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.services.others.EmailService;

@Service
public class MejlRadOdbijen implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJL DA JE RAD ODBIJEN OD UREDNIKA SERVICE");
        String autor_email = (String) execution.getVariable("email_autora");

        String message = "Hello!\n";
        message += "Article has been denied.\n";
        message += "Reason: Your article doesn't belong to our magazine.\n";

        //emailService.sendMail(autor_email, "ARTICLE DENIED", message);
        System.out.println("--Poslato na " + autor_email);
    }
}