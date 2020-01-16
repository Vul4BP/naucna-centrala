package root.demo.services.others;

import org.springframework.stereotype.Service;
import root.demo.model.Authority;
import root.demo.model.UserDb;
import root.demo.repository.UserDbRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDbService implements IUserDbService {
    private final UserDbRepository userDbRepository;

    public UserDbService(UserDbRepository userDbRepository) {
        this.userDbRepository = userDbRepository;
    }

    @Override
    public UserDb findByUsername(String username) {
        return userDbRepository.findByUsername(username);
    }

    @Override
    public UserDb save(UserDb userDb) {
        return userDbRepository.save(userDb);
    }

    @Override
    public List<UserDb> findByRole(String role) {
        List<UserDb> allUsersInRole = new ArrayList<UserDb>();
        List<UserDb> allUsers = this.userDbRepository.findAll();

        for(UserDb userDb : allUsers){
            List<Authority> authorities = (List<Authority>)userDb.getAuthorities();
            for(Authority auth : authorities){
                if(auth.getAuthority().equals(role)){
                    allUsersInRole.add(userDb);
                    break;
                }
            }
        }

        return allUsersInRole;
    }
}

