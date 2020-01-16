package root.demo.services.others;

import root.demo.model.UserDb;
import java.util.List;

public interface IUserDbService {
    UserDb findByUsername(String username);
    UserDb save(UserDb userDb);
    List<UserDb> findByRole(String role);
}

