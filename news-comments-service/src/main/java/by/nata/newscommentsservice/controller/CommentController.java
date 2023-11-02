package by.nata.newscommentsservice.controller;

import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
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
@RequestMapping("/api/v1/app/comments/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {

    private final ICommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody @Valid CommentRequestDto request) {
        log.debug("Input data for saving comment: {}", request);
        CommentResponseDto response = commentService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody @Valid CommentRequestDto request) {
        log.debug("Input data for updating comment: {}", request);
        CommentResponseDto response = commentService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long id) {
        CommentResponseDto response = commentService.getCommentById(id);
        log.debug("Getting comment of id {} from database: {}", id, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId) {
        List<CommentResponseDto> response = commentService.findAllByNewsId(newsId);
        log.debug("Getting comments for news with id {} from database: {}", newsId, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentResponseDto>> searchComments(@RequestParam(required = false) String keyword,
                                                                   @RequestParam int pageNumber,
                                                                   @RequestParam int pageSize) {
        List<CommentResponseDto> commentList = commentService.searchComment(keyword, pageNumber, pageSize);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
