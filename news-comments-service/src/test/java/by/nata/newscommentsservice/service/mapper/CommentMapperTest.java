package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.NewsRepository;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.util.CommentTestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapperImpl commentMapper;

    @Mock
    private NewsRepository newsRepository;

    @Test
    void dtoToEntity() {
        CommentRequestDto commentRequestDto = CommentTestData.createCommentRequestDto().build();

        when(newsRepository.findById(commentRequestDto.newsId())).thenReturn(Optional.of(News.builder().build()));

        Comment comment = commentMapper.dtoToEntity(commentRequestDto);

        Assertions.assertThat(comment).isNotNull();
        Assertions.assertThat(comment.getId()).isNull();
        Assertions.assertThat(comment.getText()).isEqualTo(commentRequestDto.text());
        Assertions.assertThat(comment.getUsername()).isEqualTo(commentRequestDto.username());
        Assertions.assertThat(comment.getTime()).isNull();
        Assertions.assertThat(comment.getNews()).isNotNull();
    }

    @Test
    void entityToDto() {
        Comment comment = CommentTestData.createComment()
                .withId(1L)
                .withUsername("User")
                .withText("Some text")
                .withTime(new Date())
                .withNews(News.builder().withId(1L).build())
                .build();

        CommentResponseDto commentResponseDto = commentMapper.entityToDto(comment);

        Assertions.assertThat(commentResponseDto).isNotNull();
        Assertions.assertThat(commentResponseDto.id()).isEqualTo(comment.getId());
        Assertions.assertThat(commentResponseDto.text()).isEqualTo(comment.getText());
        Assertions.assertThat(commentResponseDto.username()).isEqualTo(comment.getUsername());
        Assertions.assertThat(commentResponseDto.time()).isEqualTo(comment.getTime());
        Assertions.assertThat(commentResponseDto.newsId()).isEqualTo(comment.getNews().getId());
    }

}