package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client interface for user-related operations in non-production profiles.
 * Implements the {@link UserClientProvider} interface and extends its functionality
 * by specifying the Feign client details.
 */
@Profile("!prod")
@FeignClient(value = "user-service", url = "${spring.settings.user-service.uri}")
public interface UserClient extends UserClientProvider {

    /**
     * Retrieves user information by validating the provided authorization header.
     *
     * @param authHeader The Authorization header containing user authentication information.
     * @return An {@link AppUserResponseDto} object representing user details.
     */
    @GetMapping("/validate")
    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
