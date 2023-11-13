package by.nata.newscommentsservice.controller;

import by.nata.applicationloggingstarter.annotation.MethodLog;
import by.nata.newscommentsservice.controller.api.CommentDocOpenApi;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.validator.CommentValidation;
import by.nata.newscommentsservice.service.validator.NewsValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * The {@code CommentController} class is a Spring REST controller providing API endpoints
 * for managing comments. It implements the {@code CommentDocOpenApi} interface.
 * <p>
 * Endpoints:
 * 1. Save Comment: POST ("/")
 * 2. Update Comment: PUT ("/{id}")
 * 3. Get Comment by ID: GET ("/{id}")
 * 4. Get Comments by News ID: GET ("/news/{newsId}")
 * 5. Search Comments: GET ("/search")
 * 6. Delete Comment by ID: DELETE ("/{id}")
 * <p>
 * Logging:
 * - @MethodLog: Custom annotation for logging method input data.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/app/comments/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController implements CommentDocOpenApi {

    private final ICommentService commentService;

    /**
     * Saves a new comment.
     *
     * @param request The request body for creating a new comment.
     * @return ResponseEntity<CommentResponseDto> with the created comment and HTTP status 201 (CREATED).
     */
    @MethodLog
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER')")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody @Valid CommentRequestDto request) {
        log.debug("Input data for saving comment: {}", request);
        CommentResponseDto response = commentService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing comment.
     *
     * @param id      The ID of the comment to be updated.
     * @param request The request body for updating an existing comment.
     * @return CommentResponseDto with the updated comment details.
     */
    @MethodLog
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER') && @commentServiceImpl.getCommentById(#id).username().equals(principal.username)")
    public CommentResponseDto updateComment(
            @PathVariable @CommentValidation Long id,
            @RequestBody @Valid CommentRequestDto request
    ) {
        log.debug("Input data for updating comment: {}", request);
        return commentService.update(id, request);
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to be retrieved.
     * @return CommentResponseDto with the details of the retrieved comment.
     */
    @MethodLog
    @GetMapping("/{id}")
    public CommentResponseDto getComment(@PathVariable @CommentValidation Long id) {
        CommentResponseDto response = commentService.getCommentById(id);
        log.debug("Getting comment of id {} from database: {}", id, response);
        return response;
    }

    /**
     * Retrieves all comments for a given news ID.
     *
     * @param newsId The ID of the news for which comments are to be retrieved.
     * @return List<CommentResponseDto> containing comments for the specified news.
     */
    @MethodLog
    @GetMapping("/news/{newsId}")
    public List<CommentResponseDto> getCommentsByNewsId(@PathVariable @NewsValidation Long newsId) {
        List<CommentResponseDto> response = commentService.findAllByNewsId(newsId);
        log.debug("Getting comments for news with id {} from database: {}", newsId, response);
        return response;
    }

    /**
     * Searches for comments based on a keyword.
     *
     * @param keyword    The keyword for searching comments.
     * @param pageNumber The page number for paginated results.
     * @param pageSize   The page size for paginated results.
     * @return List<CommentResponseDto> containing comments matching the search criteria.
     */
    @MethodLog
    @GetMapping("/search")
    public List<CommentResponseDto> searchComments(
            @RequestParam(required = false) String keyword,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        return commentService.searchComment(keyword, pageNumber, pageSize);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to be deleted.
     * @return ResponseEntity<Void> with HTTP status 204 (NO CONTENT) indicating successful deletion.
     */
    @MethodLog
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER') && @commentServiceImpl.getCommentById(#id).username().equals(principal.username)")
    public ResponseEntity<Void> deleteComment(@PathVariable @CommentValidation Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
