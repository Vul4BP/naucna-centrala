package root.demo.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.demo.Dto.CasopisDto;
import root.demo.model.Casopis;
import root.demo.model.UserDb;
import root.demo.repository.CasopisRepository;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CasopisService implements ICasopisService{

    @Autowired
    private CasopisRepository casopisRepository;

    @Autowired
    UserDbService userDbService;

    @Override
    public boolean validateCasopis(CasopisDto casopisDto) {
        Casopis casopis = new Casopis();

        casopis.setNaziv(casopisDto.getNaziv());
        casopis.setIssn(casopisDto.getIssn());
        casopis.setKomeSePlaca(casopisDto.getKomeSePlaca());
        casopis.setClanarina(casopisDto.getClanarina());
        casopis.setNaciniplacanja(casopisDto.getNaciniPlacanja());
        casopis.setNaucneoblasti(casopisDto.getOblasti());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Casopis>> violations = validator.validate(casopis);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Casopis> violation : violations) {
                System.out.println("--> " + violation.getMessage());
            }

            return false;
        }

        try{
            Long.parseLong(casopis.getIssn());
        }catch (Exception e){
            System.out.println("--> Casopis issn can only contain digits");
            return false;
        }

        Casopis casopisName = casopisRepository.findByNaziv(casopis.getNaziv());
        Casopis casopisIssn = casopisRepository.findByIssn(casopis.getIssn());

        if(casopisName != null && casopisIssn != null) {
            System.out.println("--> Casopis with given name already exists");
            System.out.println("--> Casopis with given issn already exists");
            return false;
        }else if(casopisName != null) {
            System.out.println("--> Casopis with given name already exists");
            return false;
        }else if(casopisIssn != null) {
            System.out.println("--> Casopis with given issn already exists");
            return false;
        }

        return true;
    }

    @Override
    public CasopisDto addCasopis(CasopisDto casopisDto, String username) throws RollbackException {
        Casopis casopis = new Casopis();

        casopis.setNaziv(casopisDto.getNaziv());
        casopis.setIssn(casopisDto.getIssn());
        casopis.setKomeSePlaca(casopisDto.getKomeSePlaca());
        casopis.setClanarina(casopisDto.getClanarina());
        casopis.setNaciniplacanja(casopisDto.getNaciniPlacanja());
        casopis.setNaucneoblasti(casopisDto.getOblasti());
        casopis.setRecenzenti(new ArrayList<>());

        List<UserDb> urednici = new ArrayList<>();
        UserDb glavniUrednik = userDbService.findByUsername(username);
        if(glavniUrednik!=null){
            urednici.add(glavniUrednik);
        }

        casopis.setUrednici(urednici);
        casopis.setEnabled(false);
        casopisRepository.save(casopis);
        return casopisDto;
    }

    @Override
    public CasopisDto changeStatus(CasopisDto casopisDto) {
        return null;
    }
}

