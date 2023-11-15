package by.nata.newscommentsservice.controller;

import by.nata.applicationloggingstarter.annotation.MethodLog;
import by.nata.newscommentsservice.controller.api.NewsDocOpenApi;
import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import by.nata.newscommentsservice.service.validator.NewsValidation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/**
 * The {@code NewsController} class is a Spring REST controller providing API endpoints
 * for managing news. It implements the {@code NewsDocOpenApi} interface.
 * <p>
 * Logging:
 * - @MethodLog: Custom annotation for logging method input data.
 */
//@CrossOrigin
@RestController
@RequestMapping("/api/v1/app/news")
@RequiredArgsConstructor
@Slf4j
@Validated
public class NewsController implements NewsDocOpenApi {

    private final INewsService newsService;

    @MethodLog
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    public ResponseEntity<NewsResponseDto> saveNews(@RequestBody @Valid NewsRequestDto request) {
        log.debug("Input data for saving news: {}", request);
        NewsResponseDto response = newsService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @MethodLog
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('JOURNALIST') && @newsServiceImpl.getNewsById(#id).username().equals(principal.username)")
    public NewsResponseDto updateNews(
            @PathVariable @NewsValidation Long id,
            @RequestBody @Valid NewsRequestDto request) {
        log.debug("Input data for updating news: {}", request);
        return newsService.update(id, request);
    }

    @MethodLog
    @GetMapping("/{id}")
    public NewsResponseDto getNews(@PathVariable @NewsValidation Long id) {
        NewsResponseDto response = newsService.getNewsById(id);
        log.debug("Getting news section of id {} from database: {} ", id, response);
        return response;
    }

    @MethodLog
    @GetMapping
    public List<NewsResponseDto> getAllNews(@PageableDefault(size = 5) @Nullable Pageable pageable) {
        List<NewsResponseDto> response = newsService.getAllNews(pageable);
        log.debug("Getting news from database: {} ", response);
        return response;
    }

    @MethodLog
    @GetMapping("/{newsId}/comments")
    public NewsWithCommentsResponseDto getNewsWithComments(@PathVariable @NewsValidation Long newsId,
                                                           @PageableDefault(size = 5) @Nullable Pageable pageable) {
        NewsWithCommentsResponseDto response = newsService.getNewsWithComments(newsId, pageable);
        log.debug("Getting news with comments section of id {} from database: {} ", newsId, response);
        return response;
    }

    @MethodLog
    @GetMapping("/search")
    public List<NewsResponseDto> searchNews(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String dateString,
                                            @PageableDefault(size = 5) @Nullable Pageable pageable) {
        return newsService.searchNews(keyword, dateString, pageable);
    }

    @MethodLog
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('JOURNALIST') && @newsServiceImpl.getNewsById(#id).username().equals(principal.username)")
    public ResponseEntity<Void> deleteNews(@PathVariable @NewsValidation Long id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
