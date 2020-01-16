package root.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.UserDb;
import root.demo.services.others.UserDbService;

@Service
public class RegisterProcessHandler implements ExecutionListener {
    @Autowired
    IdentityService identityService;

    @Autowired
    private UserDbService userDbService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("REGISTER PROCESS HANDLER");
        execution.setVariable("validacija", false);
        System.out.println("--> postavljena variabla validacija");

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
