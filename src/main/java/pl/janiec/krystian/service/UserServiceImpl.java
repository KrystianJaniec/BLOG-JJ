package pl.janiec.krystian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.janiec.krystian.dto.UserDTO;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.models.UserRole;
import pl.janiec.krystian.repository.UserRepository;
import pl.janiec.krystian.repository.UserRoleRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User create(UserDTO userDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(userDTO.getEmail(),
                userDTO.getFullName(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()));

        UserRole defaultRole = userRoleRepository.findByRole(DEFAULT_ROLE);
        user.addRole(defaultRole);

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User profile(Authentication principal) {
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }
}
