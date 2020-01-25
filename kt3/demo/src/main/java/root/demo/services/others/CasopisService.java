package root.demo.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import root.demo.Dto.CasopisDto;
import root.demo.Dto.UserDto;
import root.demo.model.Casopis;
import root.demo.model.UserDb;
import root.demo.repository.CasopisRepository;
import root.demo.utils.ObjectMapperUtils;

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
    private UserDbService userDbService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean validateCasopis(CasopisDto casopisDto) {
        Casopis casopis = new Casopis();

        casopis.setNaziv(casopisDto.getNaziv());
        casopis.setIssn(casopisDto.getIssn());
        casopis.setKomeSePlaca(casopisDto.getKomeSePlaca());
        casopis.setClanarina(casopisDto.getClanarina());
        casopis.setNaciniplacanja(casopisDto.getNaciniPlacanja());
        casopis.setNaucneoblasti(casopisDto.getNaucneoblasti());

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
    public boolean validateEditovanjeCasopis(CasopisDto casopisDto) {
        Casopis casopis = new Casopis();

        casopis.setNaziv(casopisDto.getNaziv());
        casopis.setIssn(casopisDto.getIssn());
        casopis.setKomeSePlaca(casopisDto.getKomeSePlaca());
        casopis.setClanarina(casopisDto.getClanarina());
        casopis.setNaciniplacanja(casopisDto.getNaciniPlacanja());
        casopis.setNaucneoblasti(casopisDto.getNaucneoblasti());

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

        if(casopisName == null){
            System.out.println("--> Casopis name and issn can't be edited");
            return false;
        }else if(!casopis.getIssn().equals(casopis.getIssn())){
            System.out.println("--> Casopis name and issn can't be edited");
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
        casopis.setNaucneoblasti(casopisDto.getNaucneoblasti());
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
    public CasopisDto editCasopis(CasopisDto casopisDto) throws RollbackException {
        Casopis casopis = casopisRepository.findByNaziv(casopisDto.getNaziv());

        casopis.setKomeSePlaca(casopisDto.getKomeSePlaca());
        casopis.setClanarina(casopisDto.getClanarina());
        casopis.setNaciniplacanja(casopisDto.getNaciniPlacanja());
        casopis.setNaucneoblasti(casopisDto.getNaucneoblasti());
        casopis.setRecenzenti(new ArrayList<>());

        List<UserDb> urednici = new ArrayList<>();
        urednici.add(casopis.getUrednici().get(0));
        casopis.setUrednici(urednici);

        //casopisRepository.save(casopis);
        return casopisDto;
    }

    @Override
    public List<CasopisDto> getAllMagazines() {
        List<Casopis> casopisi = casopisRepository.findAll();
        List<CasopisDto> casopisiDto = new ArrayList<>();
        for(Casopis c : casopisi) {
            CasopisDto casopisDto = ObjectMapperUtils.map(c, CasopisDto.class);
            casopisDto.setRecenzenti(new ArrayList<>());
            casopisDto.setUrednici(new ArrayList<>());
            casopisiDto.add(casopisDto);
        }

        return casopisiDto;
    }

    @Override
    public Casopis getById(Long id) {
        return casopisRepository.getOne(id);
    }

    @Override
    public CasopisDto changeStatus(CasopisDto casopisDto) {
        return null;
    }

    @Override
    public boolean validateUredniciIRecenzenti(List<String> urednici, List<String> recenzenti) {
        if(recenzenti == null){
            System.out.println("--> Recenzenti cant be null");
            return false;
        } else if(recenzenti.size() < 2){
            System.out.println("--> Recenzenti must be >= 2 long");
            return false;
        }else if(urednici == null){
            System.out.println("--> Urednici cant be null");
            return false;
        }

        boolean indikator = true;

        for(String username : recenzenti){
            UserDb user = userDbService.findByUsername(username);
            if(user == null){
                System.out.println("--> User with username: " + username + " cant be found in database");
                indikator = false;
            }
        }

        for(String username : urednici){
            UserDb user = userDbService.findByUsername(username);
            if(user == null){
                System.out.println("--> User with username: " + username + " cant be found in database");
                indikator = false;
            }
        }

        return indikator;
    }

    @Override
    public CasopisDto addUredniciIRecenzenti(String naziv, List<String> urednici, List<String> recenzenti) {
        List<UserDb> uredniciDb = new ArrayList<>();
        List<UserDb> recenzentiDb = new ArrayList<>();

        for(String username : recenzenti){
            UserDb user = userDbService.findByUsername(username);
            recenzentiDb.add(user);
        }

        for(String username : urednici){
            UserDb user = userDbService.findByUsername(username);
            uredniciDb.add(user);
        }

        Casopis casopis = casopisRepository.findByNaziv(naziv);
        UserDb glavniUrednik = casopis.getUrednici().get(0);
        List<UserDb> uredniciFinal = new ArrayList<>();
        uredniciFinal.add(glavniUrednik);
        //uredniciFinal.addAll(uredniciDb);

        //ako je selektovao glavnog urendika da ga ne doda opet
        for(UserDb u : uredniciDb){
            if(!u.getUsername().equals(glavniUrednik.getUsername())){
                uredniciFinal.add(u);
            }
        }

        casopis.setRecenzenti(recenzentiDb);
        casopis.setUrednici(uredniciFinal);

        CasopisDto casopisDtoDto= ObjectMapperUtils.map(casopis, CasopisDto.class);
        return casopisDtoDto;
    }

    @Override
    public CasopisDto getByNaziv(String naziv) {
        Casopis casopis = casopisRepository.findByNaziv(naziv);
        CasopisDto casopisDto = ObjectMapperUtils.map(casopis, CasopisDto.class);
        for(UserDto user : casopisDto.getRecenzenti()){
            user.setToken("");
            user.setPassword("");
        }

        for(UserDto user : casopisDto.getUrednici()){
            user.setToken("");
            user.setPassword("");
        }

        return casopisDto;
    }

    public CasopisDto postujSelleru(Casopis casopis){
        String url = "https://localhost:8443/sellerservice/magazine/add";

        CasopisDto casopisDto = new CasopisDto();
        casopisDto.setId(casopis.getId());
        casopisDto.setNaziv(casopis.getNaziv());
        casopisDto.setIssn(casopis.getIssn());
        casopisDto.setClanarina(casopis.getClanarina());
        casopisDto.setNaciniPlacanja(casopis.getNaciniplacanja());

        RestTemplate rt = restTemplate;
        CasopisDto response = rt.postForObject(url, casopisDto, CasopisDto.class);
        return response;
    }
}

