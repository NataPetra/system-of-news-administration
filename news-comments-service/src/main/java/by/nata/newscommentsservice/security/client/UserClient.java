package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * The {@code UserClient} interface is a Feign client for interacting with the "user-service" API.
 */
@FeignClient(value = "user-service", url = "${spring.settings.user-service.uri}")
public interface UserClient {

    /**
     * Retrieves user details by validating the provided authorization header.
     *
     * @param authHeader The Authorization header containing user authentication information.
     * @return AppUserResponseDto representing the user details.
     */
    @GetMapping("/validate")
    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
