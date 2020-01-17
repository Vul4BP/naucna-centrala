package root.demo.services;

import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.CasopisDto;
import root.demo.Dto.FormSubmissionDto;
import root.demo.model.*;
import root.demo.services.others.CasopisService;

@Service
public class ValidateCasopisFormService implements JavaDelegate{

    @Autowired
    private CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("VALIDATE CASOPIS FORM SERVICE");
        List<FormSubmissionDto> casopis = (List<FormSubmissionDto>)execution.getVariable("casopis");

        CasopisDto casopisDto = new CasopisDto();

        for (FormSubmissionDto formField : casopis) {
            if(formField.getFieldId().equals("naziv")) {
                casopisDto.setNaziv((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("issn")) {
                casopisDto.setIssn((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("komeSePlaca")) {
                casopisDto.setKomeSePlaca((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("clanarina")) {
                Integer value = (Integer) formField.getFieldValue();
                casopisDto.setClanarina(value.longValue());
            }
            if(formField.getFieldId().equals("oblasti")) {
                Object obj = formField.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                List<NaucnaOblast> naucneOblasti = mapper.convertValue(obj, new TypeReference<List<NaucnaOblast>>() { });
                casopisDto.setNaucneoblasti(naucneOblasti);
            }
            if(formField.getFieldId().equals("placanja")) {
                Object obj = formField.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                List<NacinPlacanja> placanja = mapper.convertValue(obj, new TypeReference<List<NacinPlacanja>>() {
                });
                casopisDto.setNaciniPlacanja(placanja);
            }
        }

        boolean editovanje = (boolean)execution.getVariable("editovanje");
        if(editovanje == false){
            if(casopisService.validateCasopis(casopisDto)){
                String username = (String)execution.getVariable("username");
                CasopisDto dodatiCasopisDto = casopisService.addCasopis(casopisDto, username);
                if(dodatiCasopisDto != null) {
                    execution.setVariable("validacija1", true);
                    System.out.println("VALIDACIJA CASOPIS FORME USPESNA");
                }
            }else{
                System.out.println("VALIDACIJA CASOPIS FORME NEUSPESNA");
            }
        }else{
            if(casopisService.validateEditovanjeCasopis(casopisDto)){
                CasopisDto editovaniCasopisDto = casopisService.editCasopis(casopisDto);
                if(editovaniCasopisDto != null) {
                    execution.setVariable("validacija1", true);
                    System.out.println("VALIDACIJA CASOPIS FORME USPESNA");
                }
            }else{
                System.out.println("VALIDACIJA CASOPIS FORME NEUSPESNA");
            }
        }
    }
}
