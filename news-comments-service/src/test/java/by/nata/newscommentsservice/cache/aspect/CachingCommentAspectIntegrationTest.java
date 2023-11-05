package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static by.nata.newscommentsservice.util.CommentTestData.createCommentRequestDtoIntegr;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("aspect")
@SpringBootTest
@RunWith(SpringRunner.class)
@SqlGroup({
        @Sql(scripts = "classpath:testdata/add_news_with_comments_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CachingCommentAspectIntegrationTest {

    @SpyBean
    private ICommentService commentService;

    @Test
    @Order(1)
    @SqlGroup({
            @Sql(scripts = "classpath:testdata/add_news_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:testdata/clear_news_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    void saveWithCache() {
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
    void updateWithCache() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .withText("Updated Comment")
                .withUsername("User3")
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
    void deleteWithCache() {
        commentService.delete(4L);

        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(4L));
    }
}