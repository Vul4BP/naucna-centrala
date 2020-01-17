package root.demo.handlers;


import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.UserDbService;

import java.util.ArrayList;
import java.util.List;

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

        execution.setVariable("username","");
        execution.setVariable("editovanje",false);

        List<User> users = new ArrayList<User>();
        List<UserDb> admini = userDbService.findByRole("ROLE_ADMIN");
        for(UserDb userDb : admini){
            User user = identityService.createUserQuery().userId(userDb.getUsername()).singleResult();
            users.add(user);
        }

        execution.setVariable("admins", users);
        System.out.println("--> postavljena variabla admins");
    }
}

