package root.demo.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.model.Casopis;
import root.demo.model.Rad;
import root.demo.repository.CasopisRepository;
import root.demo.repository.RadRepository;
import root.demo.services.others.CasopisService;
import root.demo.services.others.EmailService;
import root.demo.services.others.RadService;

import java.util.List;

@Service
public class MejlRadOdobren implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Autowired
    private CasopisRepository casopisRepository;

    @Autowired
    private RadRepository radRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("SALJI MEJL DA JE RAD ODOBREN SERVICE");
        String autor_email = (String) execution.getVariable("email_autora");

        String message = "Hello!\n";
        message += "Article has been added to our magazine.\n";

        //emailService.sendMail(autor_email, "ARTICLE APPROVED", message);
        System.out.println("--Poslato na " + autor_email);

        //Dodaj rad u casopis
        String nazivCasopisa = (String) execution.getVariable("selImeCasopisa");
        String naslovRada = (String) execution.getVariable("naslov_rada");
        Casopis casopis = casopisRepository.findByNaziv(nazivCasopisa);
        Rad rad = radRepository.findOneByNaslov(naslovRada);

        if(casopis!=null && rad != null){
            List<Rad> radovi = casopis.getRadovi();
            radovi.add(rad);
            casopis.setRadovi(radovi);
            casopisRepository.save(casopis);
        }
    }
}
