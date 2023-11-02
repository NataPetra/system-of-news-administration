package by.nata.newscommentsservice.service.api;

import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;

import java.util.List;

public interface INewsService {

    NewsResponseDto save(NewsRequestDto news);

    NewsResponseDto update(Long id, NewsRequestDto news);

    NewsResponseDto getNewsById(Long id);

    List<NewsResponseDto> getAllNews(int pageNumber, int pageSize);

    NewsWithCommentsResponseDto getNewsWithComments(Long newsId, int pageNumber, int pageSize);

    List<NewsResponseDto> searchNews(String keyword, String dateString, int pageNumber, int pageSize);
}
