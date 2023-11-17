package by.nata.userservice.controller;

import by.nata.userservice.database.repository.AppUserRepository;
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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerIntegrationTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    public static final String URL_REGISTRATION = "/api/v1/app/users/register/";
    private static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    @ParameterizedTest
    @ValueSource(strings = {"admin", "journalist", "subscriber"})
    void shouldReturn201AndCorrectUsernameWhenRegisterAdministratorWithValidCredentials(String registrationUrlType) {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_REGISTRATION + registrationUrlType,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String response = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(USERNAME, response);

        userRepository.findByUsername(USERNAME).ifPresent(userRepository::delete);
    }

    @ParameterizedTest
    @ValueSource(strings = {"UsernameUsernameUsernameUsernameUsernameUsernameUsername", " "})
    void shouldReturn400WhenRegisterWhenUserNameInvalid(String username) {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(username)
                .withPassword(PASSWORD)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_REGISTRATION + "admin",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    void shouldReturn400WhenRegisterWhenUserNameIsNull() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(null)
                .withPassword(PASSWORD)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_REGISTRATION + "admin",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    void shouldReturn400WhenRegisterWhenPasswordIsNull() {
        AppUserRequestDto request = AppUserRequestDto.builder()
                .withUsername(USERNAME)
                .withPassword(null)
                .build();
        HttpEntity<AppUserRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_REGISTRATION + "admin",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }
}