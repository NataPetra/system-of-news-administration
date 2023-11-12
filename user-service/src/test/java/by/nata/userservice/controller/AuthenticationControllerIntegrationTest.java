package by.nata.userservice.controller;

import by.nata.userservice.service.JwtService;
import by.nata.userservice.service.dto.AppUserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:testdata/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class AuthenticationControllerIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String LOGIN_URL = "/api/v1/app/users/login";

    @ParameterizedTest
    @ValueSource(strings = {"admin", "journalist", "subscriber"})
    void shouldReturn200AndAuthorizationHeaderAndCorrectUsernameWhenLoginWithValidCredentials(String userCredential) {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(userCredential)
                .withPassword(userCredential)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String token = response.getHeaders().getFirst("Authorization");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(token);
        assertEquals(userCredential, jwtService.getUsername(token));
        assertNotNull(jwtService.getExpirationDate(token));
        assertEquals(userCredential, jwtService.getAuthorities(token).stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase)
                .map(s -> s.substring(5))
                .findFirst()
                .orElseThrow());
        assertEquals(userCredential, response.getBody());
    }

    @Test
    void shouldReturn401WithoutAuthorizationHeaderWhenLoginWithInValidCredentials() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername("null")
                .withPassword("userCredential")
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}