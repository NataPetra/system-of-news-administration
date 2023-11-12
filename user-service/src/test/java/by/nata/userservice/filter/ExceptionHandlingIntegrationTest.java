package by.nata.userservice.filter;

import by.nata.userservice.service.dto.AppUserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:testdata/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
class ExceptionHandlingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    public static final String ADMIN = "admin";
    public static final String JOURNALIST = "journalist";
    public static final String SUBSCRIBER = "subscriber";
    private static final String LOGIN_URL = "/api/v1/app/users/login";

    @ParameterizedTest
    @ValueSource(strings = {ADMIN, JOURNALIST, SUBSCRIBER})
    void shouldReturn200ForProtectedResourceWithSufficientRole(String userCredential) {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(userCredential)
                .withPassword(userCredential)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> loginResponse = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getHeaders().get(HttpHeaders.AUTHORIZATION));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        ResponseEntity<String> response = restTemplate.exchange(
                "/test/protected/" + userCredential,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a protected resource!", response.getBody());
    }

    @Test
    void shouldReturn401WithoutAuthorizationHeaderWhenLoginWithInValidCredentials() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername("null")
                .withPassword("null")
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn401WithoutAuthorizationHeaderWhenLoginWithValidCredentialsButUserBlock() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername("lockedUser")
                .withPassword(SUBSCRIBER)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn400WithoutAuthorizationHeaderWhenLoginWithMissingCredentials() {
        AppUserRequestDto request = AppUserRequestDto.builder().build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn401WithoutAuthorizationHeaderWhenAccessIsDenied() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(ADMIN)
                .withPassword(ADMIN)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> loginResponse = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getHeaders().get(HttpHeaders.AUTHORIZATION));

        ResponseEntity<String> response = restTemplate.exchange(
                "/test/protected/admin",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn403ForProtectedResourceWithInValidRole() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(ADMIN)
                .withPassword(ADMIN)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> loginResponse = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getHeaders().get(HttpHeaders.AUTHORIZATION));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        ResponseEntity<String> response = restTemplate.exchange(
                "/test/protected/subscriber",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}