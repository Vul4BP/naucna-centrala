package root.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.CasopisDto;
import root.demo.Dto.FormSubmissionDto;
import root.demo.Dto.UserDto;
import root.demo.config.MyConfig;
import root.demo.model.NaucnaOblast;
import root.demo.services.others.CasopisService;
import root.demo.services.others.LoginService;
import root.demo.services.others.UserDbService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidateUrednIRecService implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("VALIDATE DODAJ UREDNIKE I RECENZENTE FORM SERVICE");
        List<FormSubmissionDto> uredniciIRecenzenti = (List<FormSubmissionDto>)execution.getVariable("uredniciIRecenzenti");

        List<String> urednici = new ArrayList<>();
        List<String> recenzenti = new ArrayList<>();

        for (FormSubmissionDto formField : uredniciIRecenzenti) {
            if(formField.getFieldId().equals("recenzenti")) {
                Object obj = formField.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                recenzenti = mapper.convertValue(obj, new TypeReference<List<String>>() { });

            }
            if(formField.getFieldId().equals("urednici")) {
                Object obj = formField.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                urednici = mapper.convertValue(obj, new TypeReference<List<String>>() { });
            }
        }

        String naziv = (String)execution.getVariable("naziv");

        if(casopisService.validateUredniciIRecenzenti(urednici, recenzenti) == true){
            CasopisDto casopisDto = casopisService.addUredniciIRecenzenti(naziv,urednici,recenzenti);
            if (casopisDto != null) {
                execution.setVariable("validacija2", true);
                System.out.println("VALIDACIJA FORME ZA DODAVANJE UREDNIKA I RECENZENATA JE USPESNA");
            }else{
                System.out.println("VALIDACIJA FORME ZA DODAVANJE UREDNIKA I RECENZENATA JE NEUSPESNA");
            }
        }else{
            System.out.println("VALIDACIJA FORME ZA DODAVANJE UREDNIKA I RECENZENATA JE NEUSPESNA");
        }
    }
}

