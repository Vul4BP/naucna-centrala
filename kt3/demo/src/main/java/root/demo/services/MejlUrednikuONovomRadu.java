package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.EmailService;
import root.demo.services.others.UserDbService;

@Service
public class MejlUrednikuONovomRadu implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDbService userDbService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJL UREDNIKU O NOVOM RADU SERVICE");
        String urednik = (String) execution.getVariable("urednik_za_odabranu_naucnu_oblast");
        UserDb user = userDbService.findByUsername(urednik);
        String urednik_email = user.getEmail();

        String message = "Hello!\n";
        message += "We received new article request.\n";
        message += "Please check it out.\n";

        //emailService.sendMail(urednik_email, "NEW ARTICLE PROPOSAL", message);

        System.out.println("--Poslato na " + urednik_email);

    }
}
