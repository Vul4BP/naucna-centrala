package root.demo.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.UserDbService;

@Service
public class ConfirmedEmailService implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    UserDbService userDbService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("CONFIRMED EMAIL SERVICE");
        String username = (String) execution.getVariable("username");
        UserDb userDb = userDbService.findByUsername(username);
        if(userDb != null){
            userDb.setEnabled(true);
        }
        System.out.println("REGISTRACIJA POTVRDJENA PREKO EMAILA");
    }

}

