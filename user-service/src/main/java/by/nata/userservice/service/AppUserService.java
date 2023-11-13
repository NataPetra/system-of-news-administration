package by.nata.userservice.service;


import by.nata.userservice.database.model.AppUser;
import by.nata.userservice.database.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.withUsername;

/**
 * The {@code AppUserService} class is a service responsible for loading user details during the authentication process.
 */
@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    /**
     * Loads user details by the given username during the authentication process.
     *
     * @param username The username of the user to load.
     * @return UserDetails object containing user details.
     * @throws UsernameNotFoundException if the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with usrename '%s' not found!", username)));
        boolean isDisabled = !user.getUnblocked();

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(isDisabled)
                .accountExpired(isDisabled)
                .accountLocked(isDisabled)
                .credentialsExpired(isDisabled)
                .roles(user.getRole().name())
                .build();
    }
}

