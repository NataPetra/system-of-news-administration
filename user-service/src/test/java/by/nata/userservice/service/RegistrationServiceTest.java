package by.nata.userservice.service;

import by.nata.userservice.database.model.AppUser;
import by.nata.userservice.database.repository.AppUserRepository;
import by.nata.userservice.ex.AppUserNameExistsException;
import by.nata.userservice.service.dto.AppUserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registerAdmin() {
        AppUserRequestDto requestDto = new AppUserRequestDto("admin", "password");

        when(appUserRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser savedUser = invocation.getArgument(0);

            return AppUser.builder()
                    .withId(savedUser.getId())
                    .withUsername(savedUser.getUsername())
                    .withPassword(savedUser.getPassword())
                    .withUnblocked(savedUser.getUnblocked())
                    .withRole(savedUser.getRole())
                    .build();
        });

        String result = registrationService.registerAdmin(requestDto);

        assertEquals("admin", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "journalist", "subscriber"})
    void registerWithExistingUsername(String userCredential) {
        AppUserRequestDto requestDto = new AppUserRequestDto(userCredential, "password");

        when(appUserRepository.findByUsername(userCredential)).thenReturn(Optional.of(new AppUser()));

        assertThrows(AppUserNameExistsException.class, () -> registrationService.registerAdmin(requestDto));
    }
}