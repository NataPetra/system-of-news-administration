package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.impl.CommentServiceImpl;
import by.nata.newscommentsservice.service.impl.NewsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static by.nata.newscommentsservice.util.CommentTestData.R_SUBSCRIBER;
import static by.nata.newscommentsservice.util.CommentTestData.SUBSCRIBER;
import static by.nata.newscommentsservice.util.CommentTestData.createCommentRequestDtoIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.JOURNALIST;
import static by.nata.newscommentsservice.util.NewsTestData.R_JOURNALIST;
import static by.nata.newscommentsservice.util.NewsTestData.createNewsRequestDtoIntegr;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("aspect")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
class CachingAspectIntegrationTest {

    @SpyBean
    private CommentServiceImpl commentService;

    @SpyBean
    private NewsServiceImpl newsService;

    @Test
    @Order(1)    //Here @Order was used to check that after the test with saving the news, in the test with receiving the news, when accessing the previously saved news, the get method was not called
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    @WithMockUser(username = SUBSCRIBER, roles = {R_SUBSCRIBER})
    void saveCommentWithCache() {
        CommentResponseDto comment = commentService.save(createCommentRequestDtoIntegr());
        assertNotNull(comment);

        CommentResponseDto cachedComment = commentService.getCommentById(comment.id());
        assertNotNull(cachedComment);
        assertSame(comment, cachedComment);
    }

    @Test
    @Order(2)
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void getCommentByIdWithCache() {
        CommentResponseDto comment1 = commentService.getCommentById(2L);
        assertNotNull(comment1);

        CommentResponseDto comment2 = commentService.getCommentById(2L);
        assertNotNull(comment2);
        assertSame(comment1, comment2);

        CommentResponseDto savedComment = commentService.getCommentById(1L);
        assertNotNull(savedComment);

        verify(commentService, times(1)).getCommentById(2L);
        verify(commentService, times(0)).getCommentById(1L);  //Here we will check that previously saved news is retrieved from the cache
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    @WithMockUser(username = SUBSCRIBER, roles = {R_SUBSCRIBER})
    void updateCommentWithCache() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .withText("Updated Comment")
                .withNewsId(2L)
                .build();
        CommentResponseDto comment = commentService.update(3L, commentRequestDto);
        assertNotNull(comment);

        CommentResponseDto cachedComment = commentService.getCommentById(comment.id());
        assertNotNull(cachedComment);
        assertSame(comment, cachedComment);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    @WithMockUser(username = SUBSCRIBER, roles = {R_SUBSCRIBER})
    void deleteCommentWithCache() {
        commentService.delete(4L);

        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(4L));
    }

    @Test
    @Order(3)
    @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = JOURNALIST, roles = {R_JOURNALIST})
    void saveNewsWithCache() {
        NewsResponseDto news = newsService.save(createNewsRequestDtoIntegr());
        assertNotNull(news);

        NewsResponseDto cachedNews = newsService.getNewsById(news.id());
        assertNotNull(cachedNews);
        assertSame(news, cachedNews);
    }

    @Test
    @Order(4)
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void getNewsByIdWithCache() {
        NewsResponseDto newsById = newsService.getNewsById(2L);
        assertNotNull(newsById);

        NewsResponseDto newsById2 = newsService.getNewsById(2L);
        assertNotNull(newsById2);
        assertSame(newsById, newsById2);

        NewsResponseDto savedNews = newsService.getNewsById(1L);
        assertNotNull(savedNews);

        verify(newsService, times(1)).getNewsById(2L);
        verify(newsService, times(0)).getNewsById(1L);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    @WithMockUser(username = JOURNALIST, roles = {R_JOURNALIST})
    void updateNewsWithCache() {
        NewsRequestDto newsRequestDto = NewsRequestDto.builder()
                .withTitle("New News")
                .withText("This is a new test news")
                .build();

        NewsResponseDto news = newsService.update(1L, newsRequestDto);
        assertNotNull(news);

        NewsResponseDto cachedNews = newsService.getNewsById((news.id()));
        assertNotNull(cachedNews);
        assertSame(news, cachedNews);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    @WithMockUser(username = JOURNALIST, roles = {R_JOURNALIST})
    void deleteNewsWithCache() {
        newsService.delete(1L);

        assertThrows(EntityNotFoundException.class, () -> newsService.getNewsById((4L)));
    }
}