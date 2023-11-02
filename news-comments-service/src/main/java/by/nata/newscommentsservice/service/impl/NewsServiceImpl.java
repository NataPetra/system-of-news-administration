package by.nata.newscommentsservice.service.impl;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements INewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final ICommentService commentService;

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

    @Override
    @Transactional(readOnly = true)
    public NewsWithCommentsResponseDto getNewsWithComments(Long newsId, int pageNumber, int pageSize) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + newsId + " not found"));

        List<CommentResponseDto> comments = commentService.findByNewsIdOrderByTimeDesc(newsId, pageNumber, pageSize);

        return NewsWithCommentsResponseDto.builder()
                .withId(news.getId())
                .withTime(news.getTime())
                .withTitle(news.getTitle())
                .withText(news.getText())
                .withCommentsList(comments)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> searchNews(String keyword, String dateString, int pageNumber, int pageSize) {
        if (keyword != null && dateString != null) {
            return searchNewsByDateAndKeyword(dateString, keyword, pageNumber, pageSize);
        } else if (dateString != null) {
            return searchNewsByDate(dateString, pageNumber, pageSize);
        } else if (keyword != null) {
            return searchNewsByKeyword(keyword, pageNumber, pageSize);
        } else {
            return Collections.emptyList();
        }
    }

    private List<NewsResponseDto> searchNewsByKeyword(String keyword, int pageNumber, int pageSize) {
        Specification<News> spec = NewsSpecification.search(keyword);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());
        Page<News> newsPage = newsRepository.findAll(spec, pageable);
        return newsPage.map(newsMapper::entityToDto).getContent();
    }

    private List<NewsResponseDto> searchNewsByDate(String dateString, int pageNumber, int pageSize) {
        Date date = convertStringToDate(dateString);
        if (date == null) {
            return Collections.emptyList();
        }
        Specification<News> spec = NewsSpecification.newsCreatedOnDate(date);
        Page<News> newsPage = newsRepository.findAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by("time").descending()));
        return newsPage.getContent().stream()
                .map(newsMapper::entityToDto)
                .toList();
    }

    private List<NewsResponseDto> searchNewsByDateAndKeyword(String dateString, String keyword, int pageNumber, int pageSize) {
        Date date = convertStringToDate(dateString);
        if (date == null) {
            return Collections.emptyList();
        }
        Specification<News> dateSpec = NewsSpecification.newsCreatedOnDate(date);
        Specification<News> keywordSpec = NewsSpecification.search(keyword);
        Specification<News> combinedSpec = Specification.where(dateSpec).and(keywordSpec);
        Page<News> newsPage = newsRepository.findAll(combinedSpec, PageRequest.of(pageNumber, pageSize, Sort.by("time").descending()));
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
