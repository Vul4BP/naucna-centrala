package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.EmailService;

@Service
public class SaljiMejlove implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJLOVE SERVICE");
        String urednik_email = (String) execution.getVariable("email_glavnog_urednika");
        String autor_email = (String) execution.getVariable("email_autora");

        String message = "Hello!\n";
        message += "We received new article request.\n";
        message += "Please check it out.\n";

        //emailService.sendMail(urednik_email, "NEW ARTICLE", message);
        //emailService.sendMail(autor_email, "NEW ARTICLE", message);
        System.out.println("--Poslato na " + urednik_email);
        System.out.println("--Poslato na " + autor_email);
    }
}
