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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static by.nata.newscommentsservice.util.CommentTestData.ROLE_SUBSCRIBER;
import static by.nata.newscommentsservice.util.CommentTestData.SUBSCRIBER;
import static by.nata.newscommentsservice.util.CommentTestData.createCommentRequestDtoIntegr;
import static by.nata.newscommentsservice.util.NewsTestData.JOURNALIST;
import static by.nata.newscommentsservice.util.NewsTestData.ROLE_JOURNALIST;
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

    //TODO: написать комментрай по order
    @Test
    @Order(1)
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void saveCommentWithCache() {
        setUserInContext(ROLE_SUBSCRIBER, SUBSCRIBER);

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
        verify(commentService, times(0)).getCommentById(1L);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void updateCommentWithCache() {
        setUserInContext("ROLE_SUBSCRIBER", "subscriber");
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
    void deleteCommentWithCache() {
        setUserInContext("ROLE_SUBSCRIBER", "subscriber");
        commentService.delete(4L);

        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(4L));
    }

    @Test
    @Order(3)
    @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveNewsWithCache() {

        setUserInContext("ROLE_JOURNALIST", "journalist");

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
    void updateNewsWithCache() {

        setUserInContext("ROLE_JOURNALIST", "journalist");
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
    void deleteNewsWithCache() {
        setUserInContext(ROLE_JOURNALIST, JOURNALIST);
        newsService.delete(1L);

        assertThrows(EntityNotFoundException.class, () -> newsService.getNewsById((4L)));
    }

    private static void setUserInContext(String role, String username) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        User userDetails = new User(
                username,
                " ",
                authorities);
        userDetails.eraseCredentials();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}