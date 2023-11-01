package by.nata.newscommentsservice.service.api;

import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;

import java.util.List;

public interface INewsService {

    NewsResponseDto save(NewsRequestDto news);

    NewsResponseDto update(Long id, NewsRequestDto news);

    NewsResponseDto getNewsById(Long id);

    List<NewsResponseDto> getAllNews(int pageNumber, int pageSize);
}
