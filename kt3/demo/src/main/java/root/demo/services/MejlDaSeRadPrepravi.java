package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.services.others.EmailService;

@Service
public class MejlDaSeRadPrepravi implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJL DA RAD TREBA DA SE PREPRAVI SERVICE");
        String autor_email = (String) execution.getVariable("email_autora");

        String message = "Hello!\n";
        message += "Article needs to be fixed.\n";

        //emailService.sendMail(autor_email, "FIX ARTICLE", message);
        System.out.println("--Poslato na " + autor_email);
    }
}
