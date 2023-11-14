package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Interface representing a provider for user-related operations.
 * Implementing classes or interfaces should provide methods for user interactions.
 */
public interface UserClientProvider {

    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
