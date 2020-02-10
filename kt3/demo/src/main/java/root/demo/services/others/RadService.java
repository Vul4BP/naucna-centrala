package root.demo.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.demo.model.Casopis;
import root.demo.model.KoAutor;
import root.demo.model.Rad;
import root.demo.model.UserDb;
import root.demo.repository.KoAutorRepository;
import root.demo.repository.RadRepository;
import root.demo.repository.UserDbRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RadService implements IRadService {

    @Autowired
    private RadRepository radRepository;

    @Autowired
    private UserDbRepository userDbRepository;

    @Autowired
    private FileService fileService;

    @Override
    public Rad addRad(Rad rad, MultipartFile file, String autor) {

        rad.setPdf(file.getOriginalFilename());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Rad>> violations = validator.validate(rad);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Rad> violation : violations) {
                System.out.println("--> " + violation.getMessage());
            }
        }else {
            for(KoAutor koAutor : rad.getKoautori()){
                UserDb userDb = userDbRepository.findByImeAndEmail(koAutor.getIme(),koAutor.getEmail());
                if(userDb != null){
                    koAutor.setUserId(userDb.getId());
                }
            }

            try {
                fileService.storeFile(file);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            List<KoAutor> koAutoriZaBazu = new ArrayList<>();
            UserDb user = userDbRepository.findByUsername(autor);
            //GLAVNI AUTOR
            KoAutor koAutor = new KoAutor();
            koAutor.setIme(user.getIme());
            koAutor.setEmail(user.getEmail());
            koAutor.setDrzava(user.getDrzava());
            koAutor.setGrad(user.getGrad());
            koAutor.setUserId(user.getId());
            koAutoriZaBazu.add(koAutor);    //sa indexom 0 je glavni autor
            koAutoriZaBazu.addAll(rad.getKoautori());
            rad.setKoautori(koAutoriZaBazu);
            radRepository.save(rad);
        }

        return rad;
    }

    @Override
    public Rad editRad(Rad rad, MultipartFile file) {
        Rad radDb = radRepository.findOneByNaslov(rad.getNaslov());
        if(radDb == null){
            return null;
        }

        try {
            fileService.storeFile(file);
            radDb.setPdf(file.getOriginalFilename());
            Rad retVal = radRepository.save(radDb);
            return retVal;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
