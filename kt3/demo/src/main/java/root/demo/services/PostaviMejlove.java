package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.CasopisDto;
import root.demo.model.UserDb;
import root.demo.services.others.CasopisService;
import root.demo.services.others.UserDbService;

@Service
public class PostaviMejlove implements JavaDelegate {
    @Autowired
    private UserDbService userDbService;

    @Autowired
    private CasopisService casopisService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("POSTAVI VARIJABLE ZA MEJLOVE SERVICE");

        String imeCasopisa = (String)delegateExecution.getVariable("selImeCasopisa");
        String autor = (String)delegateExecution.getVariable("starterIdVariable");

        CasopisDto casopis = casopisService.getByNaziv(imeCasopisa);
        UserDb user = userDbService.findByUsername(autor);

        delegateExecution.setVariable("email_glavnog_urednika", casopis.getUrednici().get(0).getEmail());
        delegateExecution.setVariable("email_autora", user.getEmail());

        delegateExecution.setVariable("glavni_urednik", casopis.getUrednici().get(0).getUsername());
    }
}
