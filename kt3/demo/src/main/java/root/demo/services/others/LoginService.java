package root.demo.services.others;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.demo.Dto.UserDto;
import root.demo.config.MyConfig;
import root.demo.model.Authority;
import root.demo.model.UserDb;
import root.demo.repository.AuthorityRepository;
import root.demo.repository.NaucnaOblastRepository;
import root.demo.repository.UserDbRepository;
import root.demo.security.TokenUtils;
import root.demo.security.auth.JwtAuthenticationRequest;
import root.demo.utils.ObjectMapperUtils;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@Service
public class LoginService implements ILoginService{

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserDbRepository userDbRepository;

    @Autowired
    IdentityService identityService;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private NaucnaOblastRepository naucnaOblastRepository;

    @Override
    public UserDb checkCredentials(JwtAuthenticationRequest request) {
        UserDb user = userDbRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean validateUser(UserDto userDto) {
        UserDb userDb = new UserDb();

        userDb.setDrzava(userDto.getDrzava());
        userDb.setPrezime(userDto.getPrezime());
        userDb.setIme(userDto.getIme());
        userDb.setUsername(userDto.getUsername());
        userDb.setEnabled(false);
        userDb.setPassword(userDto.getPassword());
        if(userDto.getTitula()!=null){
            userDb.setTitula(userDto.getTitula());
        }
        userDb.setEmail(userDto.getEmail());
        userDb.setGrad(userDto.getGrad());
        userDb.setNaucneoblasti(userDto.getNaucneoblasti());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserDb>> violations = validator.validate(userDb);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserDb> violation : violations) {
                System.out.println("--> " + violation.getMessage());
            }

            return false;
        }

        UserDb userDbUsername = userDbRepository.findByUsername(userDb.getUsername());
        UserDb userDbEmail = userDbRepository.findByEmail(userDb.getEmail());

        if(userDbUsername != null && userDbEmail != null) {
            System.out.println("--> User with given username already exists");
            System.out.println("--> User with given email already exists");
            return false;
        }else if(userDbUsername != null) {
            System.out.println("--> User with given username already exists");
            return false;
        }else if(userDbEmail != null) {
            System.out.println("--> User with given email already exists");
            return false;
        }

        return true;
    }

    @Override
    public UserDto register(UserDto userDto, boolean enabled, String role) throws RollbackException {
        UserDb user = new UserDb();

        if(userDbRepository.findByUsername(userDto.getUsername()) != null){
            return userDto;
        }

        user.setIme(userDto.getIme());
        user.setPrezime(userDto.getPrezime());
        user.setEnabled(enabled);
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setGrad(userDto.getGrad());
        user.setDrzava(userDto.getDrzava());
        if(userDto.getTitula()!=null){
            user.setTitula(userDto.getTitula());
        }

        user.setNaucneoblasti(userDto.getNaucneoblasti());

        Authority authority = authorityRepository.findOneByName(role);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        User cam_user = identityService.newUser("");
        cam_user.setId(userDto.getUsername());
        cam_user.setPassword(userDto.getPassword());
        cam_user.setEmail(userDto.getEmail());
        cam_user.setLastName(userDto.getPrezime());
        cam_user.setFirstName(userDto.getIme());

        identityService.saveUser(cam_user);
        user = userDbRepository.save(user);

        //ovo sluzi za dodavanje autora prilikom registracije
        if(role.equals(MyConfig.roleAutor) == true) {
            identityService.createMembership(user.getUsername(), MyConfig.groupAutor);
        }

        return userDto;
    }

    @Override
    public UserDto login(JwtAuthenticationRequest request) {
        UserDb user = userDbRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword()) && user.isEnabled()){
                String jwt = tokenUtils.generateToken(request.getUsername());
                int expiresIn = tokenUtils.getExpiredIn();
                UserDto userDto= ObjectMapperUtils.map(user, UserDto.class);
                userDto.setPassword(null); //ne sme se vracati password, ni password hash
                userDto.setExpiresIn(expiresIn);
                userDto.setToken(jwt);
                // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
                return userDto;
            }
        }
        return null;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String username) throws Exception{
        UserDb user= userDbRepository.findByUsername(username);
        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userDbRepository.save(user);
        }else{
            throw new Exception();
        }
    }

    @Override
    public UserDto refreshAuthenticationToken(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        UserDb user = (UserDb) userDbRepository.findByUsername(username);
        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            int expiresIn = tokenUtils.getExpiredIn();
            UserDto userDto= ObjectMapperUtils.map(user,UserDto.class);
            userDto.setExpiresIn(expiresIn);
            userDto.setToken(refreshedToken);
            return userDto;
        } else {
            return null;
        }
    }

}

