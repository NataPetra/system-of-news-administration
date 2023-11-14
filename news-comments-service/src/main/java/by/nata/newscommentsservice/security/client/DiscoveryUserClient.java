package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Profile("prod")
@FeignClient(name = "user-service")
public interface DiscoveryUserClient extends UserClienProvider {

    @GetMapping("/api/v1/app/users/validate")
    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
