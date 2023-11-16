package by.nata.newscommentsservice.controller;

import by.nata.applicationloggingstarter.annotation.MethodLog;
import by.nata.newscommentsservice.controller.api.CommentDocOpenApi;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.validator.CommentValidation;
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
 * The {@code CommentController} class is a Spring REST controller providing API endpoints
 * for managing comments. It implements the {@code CommentDocOpenApi} interface.
 * <p>
 * Logging:
 * - @MethodLog: Custom annotation for logging method input data.
 */
@RestController
@RequestMapping("/api/v1/app/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController implements CommentDocOpenApi {

    private final ICommentService commentService;

    @MethodLog
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER')")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody @Valid CommentRequestDto request) {
        log.debug("Input data for saving comment: {}", request);
        CommentResponseDto response = commentService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @MethodLog
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SUBSCRIBER') && @commentServiceImpl.getCommentById(#id).username().equals(principal.username)")
    public CommentResponseDto updateComment(
            @PathVariable @CommentValidation Long id,
            @RequestBody @Valid CommentRequestDto request
    ) {
        log.debug("Input data for updating comment: {}", request);
        return commentService.update(id, request);
    }

    @MethodLog
    @GetMapping("/{id}")
    public CommentResponseDto getComment(@PathVariable @CommentValidation Long id) {
        CommentResponseDto response = commentService.getCommentById(id);
        log.debug("Getting comment of id {} from database: {}", id, response);
        return response;
    }

    @MethodLog
    @GetMapping("/news/{newsId}")
    public List<CommentResponseDto> getCommentsByNewsId(@PathVariable @NewsValidation Long newsId) {
        List<CommentResponseDto> response = commentService.findAllByNewsId(newsId);
        log.debug("Getting comments for news with id {} from database: {}", newsId, response);
        return response;
    }

    @MethodLog
    @GetMapping("/search")
    public List<CommentResponseDto> searchComments(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 5) @Nullable Pageable pageable
    ) {
        return commentService.searchComment(keyword, pageable);
    }

    @MethodLog
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SUBSCRIBER') && @commentServiceImpl.getCommentById(#id).username().equals(principal.username)")
    public ResponseEntity<Void> deleteComment(@PathVariable @CommentValidation Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
