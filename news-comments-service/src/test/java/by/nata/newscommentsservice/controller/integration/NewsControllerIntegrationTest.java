package by.nata.newscommentsservice.controller.integration;

import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class NewsControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.change-log", () -> "db.changelog/changelog-test.xml");
    }


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Long NEWS_ID = 1L;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;

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
    void shouldReturn201WhenSaveNewsSuccessful() {
        NewsRequestDto newsRequestDto = createNewsRequestDtoIntegr();

        ResponseEntity<NewsResponseDto> responseEntity = restTemplate.postForEntity(
                URL_TEMPLATE_SAVE,
                newsRequestDto,
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
    void shouldReturn200AndCorrectJsonWhenUpdateNewsSuccessful() {
        NewsRequestDto request = createNewsRequestDtoIntegr();

        ResponseEntity<NewsResponseDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.PUT,
                new HttpEntity<>(request),
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
    void shouldReturn204WhenDeleteNewsSuccessful() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_UPDATE_GET_DELETE,
                HttpMethod.DELETE,
                null,
                Void.class,
                NEWS_ID);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
