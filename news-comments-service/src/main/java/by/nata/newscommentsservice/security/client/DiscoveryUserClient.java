package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client for interacting with the user service in "prod" mode,
 * utilizing the Eureka service discovery mechanism.
 */
@Profile("prod")
@FeignClient(name = "user-service")
public interface DiscoveryUserClient extends UserClientProvider {

    /**
     * Retrieves user information by validating the provided authorization header.
     *
     * @param authHeader The Authorization header containing user authentication information.
     * @return An {@link AppUserResponseDto} object representing user details.
     */
    @GetMapping("/api/v1/app/users/validate")
    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
