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

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerAdmin(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.ADMIN);
    }

    @Transactional
    public String registerJournalist(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.JOURNALIST);
    }

    @Transactional
    public String registerSubscriber(AppUserRequestDto credentials) {
        return register(credentials, AppUserRole.SUBSCRIBER);
    }

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

