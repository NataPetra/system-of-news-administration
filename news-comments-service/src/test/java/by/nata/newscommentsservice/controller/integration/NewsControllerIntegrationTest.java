package by.nata.newscommentsservice.controller.integration;

import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jetbrains.annotations.NotNull;
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

import java.util.List;

import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_GET_ALL;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_GET_WITH_COMMENT;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_SAVE;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_SEARCH;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_UPDATE_GET_DELETE;
import static by.nata.newscommentsservice.util.NewsTestData.createExpectedNewsListIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.createExpectedNewsResponseDtoForUpdateIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.createExpectedNewsResponseDtoIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.createExpectedNewsWithCommentsResponseDtoIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.createNewsRequestDtoIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.getNewsResponseDtoIntegr;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@WireMockTest(httpPort = 8180)
class NewsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Long NEWS_ID = 1L;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String JOURNALIST = "journalist";
    public static final String ROLE_JOURNALIST = "ROLE_JOURNALIST";

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenGetNewsByIdSuccessful() {
        ResponseEntity<NewsResponseDto> responseEntity = restTemplate.getForEntity(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                NewsResponseDto.class,
                NEWS_ID);

        NewsResponseDto actualResponse = responseEntity.getBody();
        NewsResponseDto expectedResponse = getNewsResponseDtoIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenGetAllNewsSuccessful() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                URL_TEMPLATE_GET_ALL,
                HttpMethod.GET,
                null,
                String.class,
                PAGE_NUMBER,
                PAGE_SIZE);

        List<NewsResponseDto> actualResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
        List<NewsResponseDto> expectedResponse = createExpectedNewsListIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenSearchNewsSuccessful() throws JsonProcessingException {
        String keyword = "test";
        String dateString = "2023-11-03";

        ResponseEntity<String> response = restTemplate.exchange(
                URL_TEMPLATE_SEARCH,
                HttpMethod.GET,
                null,
                String.class,
                keyword,
                dateString,
                PAGE_NUMBER,
                PAGE_SIZE);

        List<NewsResponseDto> actualResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
        List<NewsResponseDto> expectedResponse = createExpectedNewsListIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void shouldReturn200AndCorrectJsonWhenGetNewsWithCommentsSuccessful() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                URL_TEMPLATE_GET_WITH_COMMENT,
                HttpMethod.GET,
                null,
                String.class,
                NEWS_ID,
                PAGE_NUMBER,
                PAGE_SIZE);

        NewsWithCommentsResponseDto actualResponse = objectMapper.readValue(response.getBody(), NewsWithCommentsResponseDto.class);
        NewsWithCommentsResponseDto expectedResponse = createExpectedNewsWithCommentsResponseDtoIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn201WhenSaveNewsSuccessful() throws JsonProcessingException {
        AppUserResponseDto user = new AppUserResponseDto(JOURNALIST, ROLE_JOURNALIST);
        wireMockResponse(user);

        NewsRequestDto newsRequestDto = createNewsRequestDtoIntegr();

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<NewsRequestDto> request = new HttpEntity<>(newsRequestDto, headers);

        ResponseEntity<NewsResponseDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_SAVE,
                HttpMethod.POST,
                request,
                NewsResponseDto.class);

        NewsResponseDto actualResponse = responseEntity.getBody();
        NewsResponseDto expectedResponse = createExpectedNewsResponseDtoIntegr();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldReturn200AndCorrectJsonWhenUpdateNewsSuccessful() throws JsonProcessingException {
        AppUserResponseDto user = new AppUserResponseDto(JOURNALIST, ROLE_JOURNALIST);
        wireMockResponse(user);

        NewsRequestDto newsRequestDto = createNewsRequestDtoIntegr();

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<NewsRequestDto> request = new HttpEntity<>(newsRequestDto, headers);

        ResponseEntity<NewsResponseDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.PUT,
                request,
                NewsResponseDto.class,
                NEWS_ID);

        NewsResponseDto actualResponse = responseEntity.getBody();
        NewsResponseDto expectedResponse = createExpectedNewsResponseDtoForUpdateIntegr();

        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldReturn204WhenDeleteNewsSuccessful() throws JsonProcessingException {
        AppUserResponseDto user = new AppUserResponseDto(JOURNALIST, ROLE_JOURNALIST);
        wireMockResponse(user);

        HttpHeaders headers = getHttpHeaders();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                NEWS_ID);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @NotNull
    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(TOKEN);
        return headers;
    }

    private void wireMockResponse(AppUserResponseDto user) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(user);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/app/users/validate"))
                .willReturn(WireMock.aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).withBody(body)));
    }
}
