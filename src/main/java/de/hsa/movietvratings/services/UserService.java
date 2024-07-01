package de.hsa.movietvratings.services;

import de.hsa.movietvratings.models.UserData;
import de.hsa.movietvratings.repositories.UserRepository;
import de.hsa.movietvratings.security.SecurityService;
import de.hsa.movietvratings.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public UserService(UserRepository users, PasswordEncoder passwordEncoder, SecurityService securityService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }

    public UserData newUser(String username, String password) {
        UserData user = new UserData(username, passwordEncoder.encode(password));
        users.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = users.findUserDataByUsername(username);
        if (user == null) {
            System.out.println("user: " + user);
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }

    public UserData getDataByUsername(String username) {
        return users.findUserDataByUsername(username);
    }

    public UserData getCurrentUser() {
        final String username = securityService.getAuthenticatedUser().getUsername();
        return getDataByUsername(username);
    }
}
