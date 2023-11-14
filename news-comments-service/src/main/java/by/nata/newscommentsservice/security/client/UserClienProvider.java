package by.nata.newscommentsservice.security.client;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserClienProvider {

    AppUserResponseDto getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}
