package by.nata.userservice.controller;

import by.nata.applicationloggingstarter.annotation.MethodLog;
import by.nata.exceptionhandlingstarter.exception.BadRequestException;
import by.nata.userservice.controller.api.AuthenticationDocOpenApi;
import by.nata.userservice.service.JwtService;
import by.nata.userservice.service.dto.AppUserRequestDto;
import by.nata.userservice.service.dto.AppUserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AuthenticationController} class provides endpoints for user authentication.
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/app/users")
public class AuthenticationController implements AuthenticationDocOpenApi {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @MethodLog
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AppUserRequestDto request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,
                            jwtService.generateAccessToken(user))
                    .body(user.getUsername());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @MethodLog
    @GetMapping("/validate")
    public AppUserResponseDto validate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid header");
        }
        final String token = authHeader.split(" ")[1].trim();
        if (!jwtService.isValid(token)) {
            throw new BadRequestException("Invalid token");
        }
        return jwtService.getUserFromToken(token);
    }
}
