package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.NewsRepository;
import by.nata.newscommentsservice.database.util.NewsSpecification;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import by.nata.newscommentsservice.service.mapper.NewsMapper;
import by.nata.newscommentsservice.util.CommentTestData;
import by.nata.newscommentsservice.util.NewsTestData;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static by.nata.newscommentsservice.util.NewsTestData.createNewsList;
import static by.nata.newscommentsservice.util.NewsTestData.createNewsResponseDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private CommentServiceImpl commentService;

    private static final Long NEWS_ID = 1L;

    @Test
    void saveNewsWhenInvokeSave() {
        NewsRequestDto requestDto = NewsTestData.createNewsRequestDto().build();
        News news = NewsTestData.createNews().build();
        NewsResponseDto responseDto = NewsTestData.createNewsResponseDto().build();

        when(newsMapper.dtoToEntity(requestDto)).thenReturn(news);
        when(newsRepository.save(news)).thenReturn(news);
        when(newsMapper.entityToDto(news)).thenReturn(responseDto);

        NewsResponseDto savedNews = newsService.save(requestDto);

        assertNotNull(savedNews);
        assertEquals(responseDto, savedNews);

        verify(newsMapper, times(1)).dtoToEntity(requestDto);
        verify(newsRepository, times(1)).save(news);
        verify(newsMapper, times(1)).entityToDto(news);
    }

    @Test
    void updateNewsWhenInvokeUpdate() {
        NewsRequestDto requestDto = NewsTestData.createNewsRequestDto().build();
        News existingNews = NewsTestData.createNews()
                .withId(NEWS_ID)
                .withTime(new Date())
                .withTitle("Title")
                .withText("Text")
                .build();
        News updatedNews = NewsTestData.createNews()
                .withId(NEWS_ID)
                .withTime(new Date())
                .withTitle("ABC")
                .withText("Very interesting news")
                .build();
        NewsResponseDto responseDto = NewsTestData.createNewsResponseDto().build();

        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(updatedNews)).thenReturn(updatedNews);
        when(newsMapper.entityToDto(updatedNews)).thenReturn(responseDto);

        NewsResponseDto updatedNewsDto = newsService.update(NEWS_ID, requestDto);

        assertNotNull(updatedNewsDto);
        assertEquals(responseDto, updatedNewsDto);

        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(newsRepository, times(1)).save(updatedNews);
        verify(newsMapper, times(1)).entityToDto(updatedNews);
    }

    @Test
    void getNewsById() {
        News news = NewsTestData.createNews()
                .withId(NEWS_ID)
                .withTime(new Date())
                .withTitle("ABC")
                .withText("Very interesting news")
                .build();
        NewsResponseDto responseDto = NewsTestData.createNewsResponseDto().build();

        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));
        when(newsMapper.entityToDto(news)).thenReturn(responseDto);

        NewsResponseDto foundNews = newsService.getNewsById(NEWS_ID);

        assertNotNull(foundNews);
        assertEquals(responseDto, foundNews);

        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(newsMapper, times(1)).entityToDto(news);
    }

    @Test
    void getAllNews() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<News> newsList = createNewsList();

        List<NewsResponseDto> expectedResponse = createNewsResponseDtoList();

        Page<News> newsPage = new PageImpl<>(newsList, pageable, newsList.size());

        when(newsRepository.findAll(pageable)).thenReturn(newsPage);
        when(newsMapper.entityToDto(any(News.class))).thenAnswer(invocation -> {
            News news = invocation.getArgument(0);
            for (NewsResponseDto responseDto : expectedResponse) {
                if (responseDto.id().equals(news.getId())) {
                    return responseDto;
                }
            }
            return null;
        });

        List<NewsResponseDto> foundNews = newsService.getAllNews(pageNumber, pageSize);

        assertNotNull(foundNews);
        assertEquals(expectedResponse, foundNews);

        verify(newsRepository, times(1)).findAll(pageable);
        verify(newsMapper, times(expectedResponse.size())).entityToDto(any(News.class));
    }

    @Test
    void getNewsWithComments() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        News news = NewsTestData.createNews().withTime(convertStringToDate()).build();
        CommentResponseDto comment1 = CommentTestData.createCommentResponseDto().build();
        CommentResponseDto comment2 = CommentTestData.createCommentResponseDto().build();

        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        when(commentService.findByNewsIdOrderByTimeDesc(1L, 0, 10))
                .thenReturn(Arrays.asList(comment1, comment2));

        NewsWithCommentsResponseDto expectedResponse = NewsWithCommentsResponseDto.builder()
                .withId(news.getId())
                .withTime(sdf.format(news.getTime()))
                .withTitle(news.getTitle())
                .withText(news.getText())
                .withCommentsList(Arrays.asList(comment1, comment2))
                .build();

        NewsWithCommentsResponseDto result = newsService.getNewsWithComments(1L, 0, 10);
        assertEquals(expectedResponse, result);
    }

    @Test
    void deleteNews() {
        News news = News.builder().withId(NEWS_ID).build();

        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));

        newsService.delete(NEWS_ID);

        verify(newsRepository).delete(news);
    }

    @Test
    void deleteNewsNotFound() {
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.delete(NEWS_ID));
    }

    @Test
    void searchNews() {
        String keyword = "text";
        int pageNumber = 0;
        int pageSize = 10;

        List<News> newsList = createNewsList();
        List<NewsResponseDto> newsResponseDtoList = createNewsResponseDtoList();
        Page<News> newsPage = new PageImpl<>(newsList);

        Specification<News> newsSpec = NewsSpecification.search(keyword, null);
        try (MockedStatic<NewsSpecification> newsSpecUtil = Mockito.mockStatic(NewsSpecification.class)) {
            newsSpecUtil.when(() -> NewsSpecification.search(keyword, null)).then(invocation -> newsSpec);

            when(newsRepository.findAll(newsSpec, PageRequest.of(pageNumber, pageSize, Sort.by("time").descending())))
                    .thenReturn(newsPage);

            when(newsMapper.entityToDto(any(News.class)))
                    .thenAnswer(invocation -> {
                        News news = (News) invocation.getArguments()[0];
                        return newsResponseDtoList.stream()
                                .filter(dto -> dto.id().equals(news.getId()))
                                .findFirst()
                                .orElse(null);
                    });

            List<NewsResponseDto> result = newsService.searchNews(keyword, null, PageRequest.of(pageNumber, pageSize));

            assertNotNull(result);
            assertEquals(3, result.size());

            for (int i = 0; i < result.size(); i++) {
                NewsResponseDto expected = newsResponseDtoList.get(i);
                NewsResponseDto actual = result.get(i);

                assertEquals(expected.id(), actual.id());
                assertEquals(expected.text(), actual.text());
                assertEquals(expected.title(), actual.title());
            }
        }
    }

    @Test
    void isNewsExistWhenNewsExists() {
        Mockito.when(newsRepository.existsById(NEWS_ID)).thenReturn(true);

        boolean result = newsService.isNewsExist(NEWS_ID);

        assertTrue(result);
    }

    @Test
    void isNewsExistWhenNewsDoesNotExist() {
        Mockito.when(newsRepository.existsById(NEWS_ID)).thenReturn(false);

        boolean result = newsService.isNewsExist(NEWS_ID);

        assertFalse(result);
    }

    private Date convertStringToDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse("2023-11-03");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}