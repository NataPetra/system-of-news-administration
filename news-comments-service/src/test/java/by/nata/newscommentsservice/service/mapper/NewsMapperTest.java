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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void entityToDto() {
        News news = NewsTestData.createNews().build();

        NewsResponseDto responseDto = newsMapper.entityToDto(news);

        assertNotNull(responseDto);
        assertEquals(news.getId(), responseDto.id());
        assertEquals(news.getTime(), responseDto.time());
        assertEquals(news.getTitle(), responseDto.title());
        assertEquals(news.getText(), responseDto.text());
    }
}