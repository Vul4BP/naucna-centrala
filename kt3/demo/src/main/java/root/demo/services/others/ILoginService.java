package root.demo.services.others;

import root.demo.Dto.UserDto;
import root.demo.model.UserDb;
import root.demo.security.auth.JwtAuthenticationRequest;
import javax.servlet.http.HttpServletRequest;

public interface ILoginService {

    public abstract UserDb checkCredentials(JwtAuthenticationRequest request);
    public abstract UserDto register(UserDto userDTO, boolean enabled, String role);
    public boolean validateUser(UserDto userDto);
    public UserDto login(JwtAuthenticationRequest request);
    public void changePassword(String oldPassword, String newPassword, String username) throws Exception;
    public UserDto refreshAuthenticationToken(HttpServletRequest request);

}

