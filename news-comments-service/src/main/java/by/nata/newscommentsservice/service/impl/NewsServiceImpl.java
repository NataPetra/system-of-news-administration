package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.NewsRepository;
import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.mapper.NewsMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements INewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public NewsResponseDto save(NewsRequestDto news) {
        return Optional.of(news)
                .map(newsMapper::dtoToEntity)
                .map(newsRepository::save)
                .map(newsMapper::entityToDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public NewsResponseDto update(Long id, NewsRequestDto news) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
        existingNews.setTitle(news.title());
        existingNews.setText(news.text());
        News updatedNews = newsRepository.save(existingNews);
        return newsMapper.entityToDto(updatedNews);
    }

    @Override
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
        return newsMapper.entityToDto(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getAllNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findAll(pageable);
        return newsPage.getContent().stream()
                .map(newsMapper::entityToDto)
                .toList();
    }
}
