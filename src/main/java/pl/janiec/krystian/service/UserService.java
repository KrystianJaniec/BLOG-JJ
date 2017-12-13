package pl.janiec.krystian.service;

import org.springframework.security.core.Authentication;
import pl.janiec.krystian.dto.UserDTO;
import pl.janiec.krystian.models.User;

public interface UserService {

    User findByEmail(String email);

    boolean emailExists(String email);

    User create(UserDTO userDTO);

    User profile(Authentication principal);

}
