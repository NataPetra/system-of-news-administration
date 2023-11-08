package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.cache.annotation.CacheableMethodDelete;
import by.nata.newscommentsservice.cache.annotation.CacheableMethodGet;
import by.nata.newscommentsservice.cache.annotation.CacheableMethodPut;
import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.repository.CommentRepository;
import by.nata.newscommentsservice.database.util.CommentSpecification;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.mapper.CommentMapper;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The {@code CommentServiceImpl} class provides the service implementation for managing comments in the application.
 *
 * <p>Usage:</p>
 * <p>- Use this service to perform various operations on comments, such as creating, updating, retrieving, and deleting comments associated with news articles.</p>
 *
 * <p>Methods:</p>
 * <p>- {@code save}: Creates a new comment based on the provided {@link CommentRequestDto} and saves it to the database. Returns a {@link CommentResponseDto} representing the saved comment.
 * <p>- {@code update}: Updates an existing comment with the specified ID based on the provided {@link CommentRequestDto}. Returns a {@link CommentResponseDto} representing the updated comment.
 * <p>- {@code getCommentById}: Retrieves a comment by its unique identifier and returns a {@link CommentResponseDto} representing the comment.
 * <p>- {@code findByNewsIdOrderByTimeDesc}: Retrieves a list of comments associated with a specific news article and orders them by time. Supports pagination.
 * <p>- {@code findAllByNewsId}: Retrieves a list of all comments associated with a specific news article.
 * <p>- {@code delete}: Deletes a comment with the specified ID.
 * <p>- {@code searchComment}: Searches for comments based on a keyword, returning a list of matching comments. Supports pagination.
 * <p>- {@code isCommentExist}: Checks if a comment with a specific ID exists in the database.
 *
 * @see ICommentService
 * @see CommentRequestDto
 * @see CommentResponseDto
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "comment")
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public static final String MESSAGE_COMMENT_NOT_FOUND = "Comment with id %d not found";

    @Override
    @Transactional
    @CacheableMethodPut
    @CachePut(key = "#result.id")
    public CommentResponseDto save(CommentRequestDto comment) {
        log.info("Call methot save() from CommentService with CommentRequestDto: {}", comment);
        return Optional.of(comment)
                .map(commentMapper::dtoToEntity)
                .map(commentRepository::save)
                .map(commentMapper::entityToDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    @CacheableMethodPut
    @CachePut(key = "#result.id")
    public CommentResponseDto update(Long id, CommentRequestDto comment) {
        log.info("Call methot update() from CommentService with id: {} and CommentRequestDto: {}", id, comment);
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        existingComment.setText(comment.text());
        Comment updatedComment = commentRepository.save(existingComment);
        log.debug("Complet methot update() from CommentService with id: {}, found and update entity Comment: {}", id, updatedComment);
        return commentMapper.entityToDto(updatedComment);
    }

    @Override
    @Transactional(readOnly = true)
    @CacheableMethodGet
    @Cacheable(key = "#id")
    public CommentResponseDto getCommentById(Long id) {
        log.info("Call methot getCommentById() from CommentService with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        log.debug("Complet methot getCommentById() from CommentService with id: {}, found entity Comment: {}", id, comment);
        return commentMapper.entityToDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByNewsIdOrderByTimeDesc(Long newsId, int pageNumber, int pageSize) {
        log.info("Call methot findByNewsIdOrderByTimeDesc() from CommentService with news id: {}, pageNumber: {}, pageSize: {}", newsId, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findByNewsIdOrderByTimeDesc(newsId, pageable);
        return commentPage.getContent().stream()
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllByNewsId(Long newsId) {
        log.info("Call methot findAllByNewsId() from CommentService with news id: {}", newsId);
        List<Comment> comments = commentRepository.findAllByNewsId(newsId);
        return comments.stream()
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    @CacheableMethodDelete
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        log.info("Call methot delete() from CommentService with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> searchComment(String keyword, int pageNumber, int pageSize) {
        log.info("Call methot searchNews() from NewsService with keyword: {}, pageNumber: {}, pageSize: {}", keyword, pageNumber, pageSize);
        Specification<Comment> spec = CommentSpecification.search(keyword);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findAll(spec, pageable);
        return commentPage.getContent().stream()
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    public boolean isCommentExist(Long id) {
        return commentRepository.existsById(id);
    }
}
