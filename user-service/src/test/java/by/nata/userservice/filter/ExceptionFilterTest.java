package by.nata.userservice.filter;

import by.nata.userservice.service.JwtService;
import by.nata.userservice.service.dto.AppUserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ExceptionFilterTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOGIN_URL = "/api/v1/app/users/login";

    //TODO: handle 403, remove constants and create a class for test data
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

//    @Test
//    void shouldReturn403WithoutAuthorizationHeaderWhenLoginWithLockedUser() {
//        AppUserRequestDto request = AppUserRequestDto.builder()
//                .withUsername("lockedUser")
//                .withPassword("subscriber")
//                .build();
//        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
//        ResponseEntity<String> loginResponse = restTemplate.exchange(
//                LOGIN_URL,
//                HttpMethod.POST,
//                requestEntity,
//                String.class);
//
//        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
//        assertNotNull(loginResponse.getHeaders().get("Authorization"));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(loginResponse.getHeaders().getFirst("Authorization"));
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "/test",
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                String.class);
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//    }

//    @Test
//    void shouldReturn403WithoutAuthorizationHeaderWhenAccessIsDenied() {
//        AppUserRequestDto request = AppUserRequestDto.builder()
//                .withUsername("admin")
//                .withPassword("admin")
//                .build();
//        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
//        ResponseEntity<String> loginResponse = restTemplate.exchange(
//                LOGIN_URL,
//                HttpMethod.POST,
//                requestEntity,
//                String.class);
//
//        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
//        assertNotNull(loginResponse.getHeaders().get("Authorization"));
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "/test/protected",
//                HttpMethod.GET,
//                new HttpEntity<>(new HttpHeaders()),
//                String.class);
//
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//    }

    @Test
    void shouldReturn401WithoutAuthorizationHeaderWhenLoginWithInsufficientAuthentication() {
        AppUserRequestDto request = AppUserRequestDto.builder().build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "journalist", "subscriber"})
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
        assertNotNull(loginResponse.getHeaders().get("Authorization"));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getHeaders().getFirst("Authorization"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/test/protected/" + userCredential,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a protected resource!", response.getBody());
    }



//    @Test
//    void shouldReturn200ForProtectedResourceWithSubscriberRole() {
//        AppUserRequestDto request = AppUserRequestDto.builder()
//                .withUsername("admin")
//                .withPassword("admin")
//                .build();
//        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
//        ResponseEntity<String> loginResponse = restTemplate.exchange(
//                LOGIN_URL,
//                HttpMethod.POST,
//                requestEntity,
//                String.class);
//
//        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
//        assertNotNull(loginResponse.getHeaders().get("Authorization"));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(loginResponse.getHeaders().getFirst("Authorization"));
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "/test/protected/subscriber",
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("This is a protected resource!", response.getBody());
//    }
}