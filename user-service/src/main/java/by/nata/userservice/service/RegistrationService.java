package by.nata.userservice.service;

import by.nata.userservice.database.enumeration.AppUserRole;
import by.nata.userservice.database.model.AppUser;
import by.nata.userservice.database.repository.AppUserRepository;
import by.nata.exceptionhandlingstarter.exception.AppUserNameExistsException;
import by.nata.userservice.service.dto.AppUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code RegistrationService} class provides methods for registering users with different roles (ADMIN, JOURNALIST, SUBSCRIBER).
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers an administrator user with the provided credentials.
     *
     * @param credentials User credentials for registration.
     * @return Registered username.
     * @throws AppUserNameExistsException if the username already exists.
     */
    @Transactional
    public String registerAdmin(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.ADMIN);
    }

    /**
     * Registers a journalist user with the provided credentials.
     *
     * @param credentials User credentials for registration.
     * @return Registered username.
     * @throws AppUserNameExistsException if the username already exists.
     */
    @Transactional
    public String registerJournalist(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.JOURNALIST);
    }

    /**
     * Registers a subscriber user with the provided credentials.
     *
     * @param credentials User credentials for registration.
     * @return Registered username.
     * @throws AppUserNameExistsException if the username already exists.
     */
    @Transactional
    public String registerSubscriber(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.SUBSCRIBER);
    }

    /**
     * Generic method for registering a user with the specified role.
     *
     * @param credentials User credentials for registration.
     * @param role        Role of the user being registered.
     * @return Registered username.
     * @throws AppUserNameExistsException if the username already exists.
     */
    private String register(AppUserRequestDto credentials, AppUserRole role) {
        String username = credentials.username();
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new AppUserNameExistsException(String.format("Username '%s' already exists", username));
        }
        AppUser userToCreate = AppUser.builder().withUsername(username)
                .withPassword(passwordEncoder.encode(credentials.password()))
                .withRole(role)
                .withUnblocked(true)
                .build();
        return appUserRepository.save(userToCreate).getUsername();
    }
}

