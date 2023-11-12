package by.nata.newscommentsservice.controller.integration;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.List;

import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_GET_BY_NEWS_ID;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_SAVE;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_SEARCH;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_UPDATE_GET_DELETE;
import static by.nata.newscommentsservice.util.CommentTestData.createCommentRequestDtoIntegr;
import static by.nata.newscommentsservice.util.CommentTestData.createCommentResponseDtoIntegr;
import static by.nata.newscommentsservice.util.CommentTestData.createExpectedUpdatedCommentResponseDto;
import static by.nata.newscommentsservice.util.CommentTestData.createUpdatedCommentRequestDto;
import static by.nata.newscommentsservice.util.NewsTestData.createExpectedCommentsListIntegr;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class CommentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer wireMockServer;

    public static final int COMMENT_ID = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 2;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8180);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8180);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenGetNewsByIdSuccessful() {
        ResponseEntity<CommentResponseDto> responseEntity = restTemplate.getForEntity(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                CommentResponseDto.class,
                COMMENT_ID);

        CommentResponseDto actualResponse = responseEntity.getBody();
        CommentResponseDto expectedResponse = createCommentResponseDtoIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenGetCommentsByNewsIdSuccessful() throws JsonProcessingException {
        Long newsId = 1L;

        ResponseEntity<String> response = restTemplate.exchange(
                URL_TEMPLATE_GET_BY_NEWS_ID,
                HttpMethod.GET,
                null,
                String.class,
                newsId);

        List<CommentResponseDto> actualResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
        List<CommentResponseDto> expectedResponse = createExpectedCommentsListIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenSearchCommentsSuccessful() throws JsonProcessingException {
        String keyword = "user";

        ResponseEntity<String> response = restTemplate.exchange(
                URL_TEMPLATE_SEARCH,
                HttpMethod.GET,
                null,
                String.class,
                keyword,
                PAGE_NUMBER,
                PAGE_SIZE);

        List<CommentResponseDto> actualResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
        List<CommentResponseDto> expectedResponse = createExpectedCommentsListIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn201AndCorrectJsonWhenSaveCommentSuccessful() throws JsonProcessingException {
        AppUserResponseDto user = new AppUserResponseDto("user", "ROLE_ADMIN");
        wireMockResponse(user);

        CommentRequestDto commentRequestDto = createCommentRequestDtoIntegr();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        HttpEntity<CommentRequestDto> request = new HttpEntity<>(commentRequestDto, headers);

        ResponseEntity<CommentResponseDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_SAVE,
                HttpMethod.POST,
                request,
                CommentResponseDto.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenUpdateCommentSuccessful() {
        CommentRequestDto commentRequestDto = createUpdatedCommentRequestDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CommentRequestDto> request = new HttpEntity<>(commentRequestDto, headers);

        ResponseEntity<CommentResponseDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.PUT,
                request,
                CommentResponseDto.class,
                COMMENT_ID);

        CommentResponseDto actualResponse = responseEntity.getBody();
        CommentResponseDto expectedResponse = createExpectedUpdatedCommentResponseDto();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn204WhenDeleteCommentSuccessful() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.DELETE,
                null,
                Void.class,
                COMMENT_ID);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    private void wireMockResponse(AppUserResponseDto user) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(user);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/app/users/validate"))
                .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json").withBody(body)));
    }

}
