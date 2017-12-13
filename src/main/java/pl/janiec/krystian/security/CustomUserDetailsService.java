package pl.janiec.krystian.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.janiec.krystian.models.User;
import pl.janiec.krystian.models.UserRole;
import pl.janiec.krystian.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {
	
	private UserRepository userRepository;
	
	@Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if(user == null)
			throw new UsernameNotFoundException("User not found");
		org.springframework.security.core.userdetails.User userDetails = 
				new org.springframework.security.core.userdetails.User(
						user.getEmail(),
						user.getPassword(),
						convertAuthorities(user.getRoles()));
		return userDetails;
	}
	
	private Set<GrantedAuthority> convertAuthorities(Set<UserRole> userRoles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(UserRole ur: userRoles) {
            authorities.add(new SimpleGrantedAuthority(ur.getRole()));
        }
        return authorities;
    }

}
