package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.util.NewsTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static by.nata.newscommentsservice.util.DateFormater.formatDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class NewsMapperTest {

    @InjectMocks
    private NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @Test
    void dtoToEntity() {
        NewsRequestDto requestDto = NewsTestData.createNewsRequestDto().build();

        News news = newsMapper.dtoToEntity(requestDto);

        assertNotNull(news);
        assertNull(news.getId());
        assertNull(news.getTime());
        assertNull(news.getComments());
        assertEquals(requestDto.title(), news.getTitle());
        assertEquals(requestDto.text(), news.getText());
        //assertEquals(requestDto.username(), news.getUsername());
    }

    @Test
    void entityToDto() {
        News news = NewsTestData.createNews()
                .withId(1L)
                .withTitle("News")
                .withText("Some text")
                .withTime(new Date())
                .build();

        NewsResponseDto responseDto = newsMapper.entityToDto(news);

        assertNotNull(responseDto);
        assertEquals(news.getId(), responseDto.id());
        assertEquals(formatDate(news.getTime()), responseDto.time());
        assertEquals(news.getTitle(), responseDto.title());
        assertEquals(news.getText(), responseDto.text());
        assertEquals(news.getUsername(), responseDto.username());
    }
}