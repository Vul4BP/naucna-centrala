package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.Casopis;
import root.demo.model.UserDb;
import root.demo.repository.CasopisRepository;
import root.demo.services.others.CasopisService;

import java.util.List;

@Service
public class ProveraAktivneClanarine  implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("PROVERA AKTIVNE CLANARINE SERVICE");

        delegateExecution.setVariable("aktivna_clanarina", false);
        String nazivCasopisa = (String)delegateExecution.getVariable("selImeCasopisa");
        String username = (String)delegateExecution.getVariable("starterIdVariable");

        Casopis casopis = casopisRepository.findByNaziv(nazivCasopisa);
        if(casopis != null) {
            List<UserDb> korisniciSaAktivnomClanarinom = casopis.getKorisniciSaAktivnomClanarinom();
            for(UserDb user : korisniciSaAktivnomClanarinom){
                if(user.getUsername().equals(username) == true){
                    delegateExecution.setVariable("aktivna_clanarina", true);
                    break;
                }
            }
        }
    }
}
