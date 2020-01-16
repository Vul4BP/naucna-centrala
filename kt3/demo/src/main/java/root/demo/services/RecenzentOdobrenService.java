package root.demo.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.config.MyConfig;
import root.demo.model.Authority;
import root.demo.model.UserDb;
import root.demo.repository.AuthorityRepository;
import root.demo.services.others.UserDbService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecenzentOdobrenService implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    UserDbService userDbService;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("RECENZENT ODOBREN");
        String username = (String) execution.getVariable("username");
        UserDb userDb = userDbService.findByUsername(username);
        if(userDb != null){
            //List<Authority> authorities = (List<Authority>)userDb.getAuthorities();
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(authorityRepository.findOneByName(MyConfig.roleRecenzent));
            userDb.setAuthorities(authorities);
            System.out.println("KORISNIKU DODELJENA ULOGA RECENZENTA");
        }
        System.out.println("PROCES REGISTRACIJE ZAVRSEN");
    }

}
