package by.nata.userservice.controller;

import by.nata.applicationloggingstarter.annotation.MethodLog;
import by.nata.userservice.controller.api.RegistrationDocOpenApi;
import by.nata.userservice.service.RegistrationService;
import by.nata.userservice.service.dto.AppUserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/app/users/register")
public class RegistrationController implements RegistrationDocOpenApi {

    private final RegistrationService registrationService;

    @MethodLog
    @PostMapping(value = "/admin", produces = "application/json")
    public ResponseEntity<String> registerAdministrator(@RequestBody @Valid AppUserRequestDto request) {
        String registeredUsername = registrationService.registerAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUsername);
    }

    @MethodLog
    @PostMapping(value = "/journalist", produces = "application/json")
    public ResponseEntity<String> registerJournalist(@RequestBody @Valid AppUserRequestDto request) {
        String registeredUsername = registrationService.registerJournalist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUsername);
    }

    @MethodLog
    @PostMapping(value = "/subscriber", produces = "application/json")
    public ResponseEntity<String> registerSubscriber(@RequestBody @Valid AppUserRequestDto request) {
        String registeredUsername = registrationService.registerSubscriber(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUsername);
    }
}

