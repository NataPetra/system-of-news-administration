package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.cache.annotation.CacheableMethodDelete;
import by.nata.newscommentsservice.cache.annotation.CacheableMethodGet;
import by.nata.newscommentsservice.cache.annotation.CacheableMethodPut;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.NewsRepository;
import by.nata.newscommentsservice.database.util.NewsSpecification;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import by.nata.newscommentsservice.service.mapper.NewsMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "news")
public class NewsServiceImpl implements INewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final ICommentService commentService;

    public static final String MESSAGE_NEWS_NOT_FOUND = "News with id %d not found";

    @Override
    @Transactional
    @CacheableMethodPut
    @CachePut(key = "#result.id")
    public NewsResponseDto save(NewsRequestDto news) {
        log.info("Call methot save() from NewsService with NewsRequestDto: {}", news);
        return Optional.of(news)
                .map(newsMapper::dtoToEntity)
                .map(newsRepository::save)
                .map(newsMapper::entityToDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    @CacheableMethodPut
    @CachePut(key = "#result.id")
    public NewsResponseDto update(Long id, NewsRequestDto news) {
        log.info("Call methot update() from NewsService with id: {} and NewsRequestDto: {}", id, news);
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_NEWS_NOT_FOUND, id)));
        existingNews.setTitle(news.title());
        existingNews.setText(news.text());
        News updatedNews = newsRepository.save(existingNews);
        log.debug("Complet methot update() from NewsService with id: {}, found and update entity News: {}", id, updatedNews);
        return newsMapper.entityToDto(updatedNews);
    }

    @Override
    @Transactional(readOnly = true)
    @CacheableMethodGet
    @Cacheable(key = "#id")
    public NewsResponseDto getNewsById(Long id) {
        log.info("Call methot getNewsById() from NewsService with id: {}", id);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_NEWS_NOT_FOUND, id)));
        log.debug("Complet methot getNewsById() from NewsService with id: {}, found entity News: {}", id, news);
        return newsMapper.entityToDto(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getAllNews(int pageNumber, int pageSize) {
        log.info("Call methot getAllNews() from NewsService with pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findAll(pageable);
        return newsPage.getContent().stream()
                .map(newsMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NewsWithCommentsResponseDto getNewsWithComments(Long newsId, int pageNumber, int pageSize) {
        log.info("Call methot getNewsWithComments() from NewsService with id: {}, pageNumber: {}, pageSize: {}", newsId, pageNumber, pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_NEWS_NOT_FOUND, newsId)));

        List<CommentResponseDto> comments = commentService.findByNewsIdOrderByTimeDesc(newsId, pageNumber, pageSize);

        return NewsWithCommentsResponseDto.builder()
                .withId(news.getId())
                .withTime(sdf.format(news.getTime()))
                .withTitle(news.getTitle())
                .withText(news.getText())
                .withCommentsList(comments)
                .build();
    }

    @Override
    @Transactional
    @CacheableMethodDelete
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        log.info("Call methot delete() from NewsService with id: {}", id);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_NEWS_NOT_FOUND, id)));
        newsRepository.delete(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> searchNews(String keyword, String dateString, int pageNumber, int pageSize) {
        log.info("Call methot searchNews() from NewsService with keyword: {}, dateString: {}, pageNumber: {}, pageSize: {}", keyword, dateString, pageNumber, pageSize);
        Specification<News> spec = NewsSpecification.search(keyword, convertStringToDate(dateString));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());
        Page<News> newsPage = newsRepository.findAll(spec, pageable);
        return newsPage.getContent().stream()
                .map(newsMapper::entityToDto)
                .toList();
    }

    private Date convertStringToDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
