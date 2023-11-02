package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.CommentRepository;
import by.nata.newscommentsservice.database.util.CommentSpecification;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.mapper.CommentMapper;
import by.nata.newscommentsservice.util.CommentTestData;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    public static final Long COMMENT_ID = 1L;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Test
    void saveCommentWhenInvokeSave() {
        CommentRequestDto commentRequestDto = CommentTestData.createCommentRequestDto().build();
        Comment comment = CommentTestData.createComment().build();
        CommentResponseDto responseDto = CommentTestData.createCommentResponseDto().build();

        when(commentMapper.dtoToEntity(commentRequestDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.entityToDto(comment)).thenReturn(responseDto);

        CommentResponseDto savedComment = commentService.save(commentRequestDto);

        Assertions.assertThat(savedComment)
                .isNotNull()
                .isEqualTo(responseDto);

        verify(commentMapper, times(1)).dtoToEntity(commentRequestDto);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).entityToDto(comment);
    }

    @Test
    void updateComment() {
        CommentRequestDto commentRequestDto = CommentTestData.createCommentRequestDto().build();
        Comment comment = CommentTestData.createComment()
                .withId(COMMENT_ID)
                .withUsername("User123")
                .withText("Comment")
                .withTime(new Date())
                .withNews(News.builder().withId(1L).build())
                .build();
        Comment updatedComment = CommentTestData.createComment()
                .withId(COMMENT_ID)
                .withUsername("User123")
                .withText("Sample Comment")
                .withTime(new Date())
                .withNews(News.builder().withId(1L).build())
                .build();
        CommentResponseDto responseDto = CommentTestData.createCommentResponseDto().build();

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentMapper.entityToDto(updatedComment)).thenReturn(responseDto);

        CommentResponseDto updatedCommentDto = commentService.update(COMMENT_ID, commentRequestDto);

        Assertions.assertThat(updatedCommentDto)
                .isNotNull()
                .isEqualTo(responseDto);

        verify(commentRepository, times(1)).findById(COMMENT_ID);
        verify(commentRepository, times(1)).save(updatedComment);
        verify(commentMapper, times(1)).entityToDto(updatedComment);
    }

    @Test
    void getCommentById() {
        Comment comment = CommentTestData.createComment()
                .withId(COMMENT_ID)
                .withText("Sample Comment")
                .withUsername("User123")
                .withTime(new Date())
                .withNews(News.builder().withId(1L).build())
                .build();
        CommentResponseDto responseDto = CommentTestData.createCommentResponseDto().build();

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentMapper.entityToDto(comment)).thenReturn(responseDto);

        CommentResponseDto retrievedComment = commentService.getCommentById(COMMENT_ID);

        Assertions.assertThat(retrievedComment)
                .isNotNull()
                .isEqualTo(responseDto);

        verify(commentRepository, times(1)).findById(COMMENT_ID);
        verify(commentMapper, times(1)).entityToDto(comment);
    }

    @Test
    void findByNewsIdOrderByTimeDesc() {
        Long newsId = 1L;
        int pageNumber = 1;
        int pageSize = 10;
        Page<Comment> commentPage = new PageImpl<>(CommentTestData.createCommentList());

        when(commentRepository.findByNewsIdOrderByTimeDesc(newsId, PageRequest.of(pageNumber, pageSize))).thenReturn(commentPage);

        List<CommentResponseDto> commentResponseDtoList = commentService.findByNewsIdOrderByTimeDesc(newsId, pageNumber, pageSize);

        Assertions.assertThat(commentResponseDtoList)
                .isNotEmpty()
                .hasSize(commentPage.getNumberOfElements());

        verify(commentRepository, times(1)).findByNewsIdOrderByTimeDesc(newsId, PageRequest.of(pageNumber, pageSize));
    }

    @Test
    void findAllByNewsId() {
        Long newsId = 1L;
        List<Comment> comments = CommentTestData.createCommentList();

        when(commentRepository.findAllByNewsId(newsId)).thenReturn(comments);

        List<CommentResponseDto> commentResponseDtoList = commentService.findAllByNewsId(newsId);

        Assertions.assertThat(commentResponseDtoList)
                .isNotEmpty()
                .hasSize(comments.size());

        verify(commentRepository, times(1)).findAllByNewsId(newsId);
    }

    @Test
    void deleteComment() {
        Comment comment = Comment.builder().withId(COMMENT_ID).build();

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        commentService.delete(COMMENT_ID);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteCommentNotFound() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.delete(COMMENT_ID));
    }

    @Test
    void searchComment() {
        List<Comment> commentList = CommentTestData.createCommentList();
        List<CommentResponseDto> commentResponseDtoList = CommentTestData.createCommentResponseDtoList();

        String keyword = "test";
        int pageNumber = 0;
        int pageSize = 10;
        Page<Comment> commentPage = new PageImpl<>(commentList);

        Specification<Comment> commentSpec = CommentSpecification.search(keyword);
        try (MockedStatic<CommentSpecification> commSpecUtil = Mockito.mockStatic(CommentSpecification.class)) {
            commSpecUtil.when(() -> CommentSpecification.search(keyword)).then(invocation -> commentSpec);

            when(commentRepository.findAll(commentSpec, PageRequest.of(pageNumber, pageSize)))
                    .thenReturn(commentPage);

            when(commentMapper.entityToDto(any(Comment.class)))
                    .thenAnswer(invocation -> {
                        Comment comment = (Comment) invocation.getArguments()[0];
                        return commentResponseDtoList.stream()
                                .filter(dto -> dto.id().equals(comment.getId()))
                                .findFirst()
                                .orElse(null);
                    });


            List<CommentResponseDto> result = commentService.searchComment(keyword, pageNumber, pageSize);

            assertNotNull(result);
            assertEquals(commentList.size(), result.size());

            for (int i = 0; i < commentList.size(); i++) {
                CommentResponseDto expected = commentResponseDtoList.get(i);
                CommentResponseDto actual = result.get(i);

                assertEquals(expected.id(), actual.id());
                assertEquals(expected.text(), actual.text());
                assertEquals(expected.username(), actual.username());
                assertEquals(expected.time(), actual.time());
                assertEquals(expected.newsId(), actual.newsId());
            }

            verify(commentRepository, times(1)).findAll(commentSpec, PageRequest.of(pageNumber, pageSize));
            verify(commentMapper, times(commentList.size())).entityToDto(any(Comment.class));
        }
    }
}
