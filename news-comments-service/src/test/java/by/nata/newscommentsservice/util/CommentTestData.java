package by.nata.newscommentsservice.util;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CommentTestData {

    private CommentTestData() {
    }

    public static Comment.CommentBuilder createComment() {
        return Comment.builder();
    }

    public static CommentRequestDto.CommentRequestDtoBuilder createCommentRequestDto() {
        return CommentRequestDto.builder()
                .withText("Sample Comment")
                .withUsername("User123")
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
}
