package by.nata.newscommentsservice.util;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static by.nata.newscommentsservice.util.DateFormater.formatDate;

public final class CommentTestData {

    public static final String URL_TEMPLATE_SAVE = "/api/v1/app/comments/";
    public static final String URL_TEMPLATE_UPDATE_GET_DELETE = "/api/v1/app/comments/{id}";
    public static final String URL_TEMPLATE_GET_BY_NEWS_ID = "/api/v1/app/comments/news/{newsId}";
    public static final String URL_TEMPLATE_SEARCH = "/api/v1/app/comments/search?keyword={keyword}&pageNumber={pageNumber}&pageSize={pageSize}";
    public static final String ROLE_SUBSCRIBER = "ROLE_SUBSCRIBER";
    public static final String R_SUBSCRIBER = "SUBSCRIBER";
    public static final String SUBSCRIBER = "subscriber";
    public static final int COMMENT_ID = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 2;

    private CommentTestData() {
    }

    public static Comment.CommentBuilder createComment() {
        return Comment.builder();
    }

    public static CommentRequestDto.CommentRequestDtoBuilder createCommentRequestDto() {
        return CommentRequestDto.builder()
                .withText("Sample Comment")
                .withNewsId(1L);
    }

    public static CommentResponseDto.CommentResponseDtoBuilder createCommentResponseDto() {
        return CommentResponseDto.builder()
                .withId(1L)
                .withText("Sample Comment")
                .withUsername("User123")
                .withTime("2023-11-03 18:56:11")
                .withNewsId(1L);
    }

    public static List<Comment> createCommentList() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder()
                .withId(1L)
                .withText("Comment1 test")
                .withUsername("User123 test")
                .withTime(new Date())
                .withNews(News.builder().withId(1L).build())
                .build());
        commentList.add(Comment.builder()
                .withId(2L)
                .withText("Comment2 test")
                .withUsername("User456 test")
                .withTime(new Date())
                .withNews(News.builder().withId(2L).build())
                .build());
        return commentList;
    }

    public static List<CommentResponseDto> createCommentResponseDtoList() {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        commentResponseDtoList.add(CommentResponseDto.builder()
                .withId(1L)
                .withText("Comment1 test")
                .withUsername("User123 test")
                .withTime("2023-11-03 18:56:11")
                .withNewsId(1L)
                .build());
        commentResponseDtoList.add(CommentResponseDto.builder()
                .withId(2L)
                .withText("Comment2 test")
                .withUsername("User456 test")
                .withTime("2023-11-03 18:56:11")
                .withNewsId(2L)
                .build());
        return commentResponseDtoList;
    }

    public static CommentResponseDto createCommentResponseDtoIntegr() {
        return CommentResponseDto.builder()
                .withId(1L)
                .withText("Comment 1 for News 1")
                .withUsername("subscriber")
                .withTime("2023-11-03 01:46:22")
                .withNewsId(1L)
                .build();
    }

    public static CommentRequestDto createCommentRequestDtoIntegr() {
        return CommentRequestDto.builder()
                .withText("New Comment")
                .withNewsId(1L)
                .build();
    }

    public static CommentResponseDto createExpectedCommentResponseDto() {
        return CommentResponseDto.builder()
                .withId(1L)
                .withText("New Comment")
                .withUsername("User1")
                .withTime(formatDate(new Date()))
                .withNewsId(1L)
                .build();
    }

    public static CommentRequestDto createUpdatedCommentRequestDto() {
        return CommentRequestDto.builder()
                .withText("Updated Comment")
                .withNewsId(1L)
                .build();
    }

    public static CommentResponseDto createExpectedUpdatedCommentResponseDto() {
        return CommentResponseDto.builder()
                .withId(1L)
                .withText("Updated Comment")
                .withUsername("subscriber")
                .withTime("2023-11-03 01:46:22")
                .withNewsId(1L)
                .build();
    }
}
