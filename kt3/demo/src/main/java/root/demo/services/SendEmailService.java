package root.demo.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.EmailService;
import root.demo.services.others.UserDbService;

@Service
public class SendEmailService implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    UserDbService userDbService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SEND EMAIL SERVICE");
        String username = (String) execution.getVariable("username");
        //System.out.println(username);
        UserDb userDb = userDbService.findByUsername(username);
        //System.out.println(userDb.getLinkPotvrde());

        String link = "http://localhost:8080/registration/confirm/" + execution.getProcessInstanceId();
        String message = "Hello!\n";
        message += "We have received a request to confirm your account - " + userDb.getUsername() + "\n";
        //message += "Confirmation code will expire in 24 hours after a request was made.\n";
        message += "Click link below to confirm registration:\n";
        message += link;

        emailService.sendMail(userDb.getEmail(), "CONFIRM REGISTRATION", message);

    }

}
