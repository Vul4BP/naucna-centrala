package root.demo.services;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.FormSubmissionDto;
import root.demo.Dto.UserDto;
import root.demo.config.MyConfig;
import root.demo.model.NaucnaOblast;
import root.demo.services.others.LoginService;
import root.demo.services.others.UserDbService;

@Service
public class ValidateRegistrationFormService implements JavaDelegate{

    @Autowired
    IdentityService identityService;

    @Autowired
    UserDbService userDbService;

    @Autowired
    LoginService loginService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("VALIDATE REGISTRATION FORM SERVICE");
        List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");

        UserDto userDto = new UserDto();

        for (FormSubmissionDto formField : registration) {
            if(formField.getFieldId().equals("username")) {
                userDto.setUsername((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("password")) {
                userDto.setPassword((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("ime")) {
                userDto.setIme((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("prezime")) {
                userDto.setPrezime((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("email")) {
                userDto.setEmail((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("grad")) {
                userDto.setGrad((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("drzava")) {
                userDto.setDrzava((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("titula")) {
                userDto.setTitula((String)formField.getFieldValue());
            }
            if(formField.getFieldId().equals("oblasti")) {
                Object obj = formField.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                List<NaucnaOblast> naucneOblasti = mapper.convertValue(obj, new TypeReference<List<NaucnaOblast>>() { });
                userDto.setNaucneoblasti(naucneOblasti);
            }
        }

        if(loginService.validateUser(userDto)){
            UserDto retUserDto = loginService.register(userDto, false, MyConfig.roleAutor); //registruje se kao autor autor
            if (retUserDto != null) {
                execution.setVariable("validacija", true);
                System.out.println("VALIDACIJA REGISTRACIONE FORME USPESNA");
            }else{
                System.out.println("VALIDACIJA REGISTRACIONE FORME NEUSPESNA");
            }
        }else{
            System.out.println("VALIDACIJA REGISTRACIONE FORME NEUSPESNA");
        }
    }
}
