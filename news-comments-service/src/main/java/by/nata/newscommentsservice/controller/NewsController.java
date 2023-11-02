package by.nata.newscommentsservice.controller;

import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app/news/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class NewsController {

    private final INewsService newsService;

    @PostMapping
    public ResponseEntity<NewsResponseDto> saveNews(@RequestBody @Valid NewsRequestDto request) {
        log.debug("Input data for saving news: {}", request);
        NewsResponseDto response = newsService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public NewsResponseDto updateNews(@PathVariable Long id, @RequestBody @Valid NewsRequestDto request) {
        log.debug("Input data for updating news: {}", request);
        return newsService.update(id, request);
    }

    @GetMapping("/{id}")
    public NewsResponseDto getNews(@PathVariable Long id) {
        NewsResponseDto response = newsService.getNewsById(id);
        log.debug("Getting news section of id {} from database: {} ", id, response);
        return response;
    }

    @GetMapping
    public List<NewsResponseDto> getAllNews(@RequestParam int pageNumber,
                                            @RequestParam int pageSize) {
        List<NewsResponseDto> response = newsService.getAllNews(pageNumber, pageSize);
        log.debug("Getting news from database: {} ", response);
        return response;
    }

    @GetMapping("/{newsId}/comments")
    public NewsWithCommentsResponseDto getNewsWithComments(@PathVariable Long newsId,
                                                           @RequestParam int pageNumber,
                                                           @RequestParam int pageSize) {
        NewsWithCommentsResponseDto response = newsService.getNewsWithComments(newsId, pageNumber, pageSize);
        log.debug("Getting news with comments section of id {} from database: {} ", newsId, response);
        return response;
    }

    @GetMapping("/search")
    public List<NewsResponseDto> searchNews(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String dateString,
                                            @RequestParam int pageNumber,
                                            @RequestParam int pageSize) {
        return newsService.searchNews(keyword, dateString, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
