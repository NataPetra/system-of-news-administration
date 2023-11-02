package by.nata.newscommentsservice.service.impl;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.repository.CommentRepository;
import by.nata.newscommentsservice.database.util.CommentSpecification;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.mapper.CommentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public static final String MESSAGE_COMMENT_NOT_FOUND = "Comment with id %d not found";

    @Override
    @Transactional
    public CommentResponseDto save(CommentRequestDto comment) {
        return Optional.of(comment)
                .map(commentMapper::dtoToEntity)
                .map(commentRepository::save)
                .map(commentMapper::entityToDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public CommentResponseDto update(Long id, CommentRequestDto comment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        existingComment.setText(comment.text());
        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.entityToDto(updatedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        return commentMapper.entityToDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByNewsIdOrderByTimeDesc(Long newsId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findByNewsIdOrderByTimeDesc(newsId, pageable);
        return commentPage.getContent().stream()
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllByNewsId(Long newsId) {
        List<Comment> comments = commentRepository.findAllByNewsId(newsId);
        return comments.stream()
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MESSAGE_COMMENT_NOT_FOUND, id)));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> searchComment(String keyword, int pageNumber, int pageSize) {
        Specification<Comment> spec = CommentSpecification.search(keyword);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findAll(spec, pageable);

        return commentPage.getContent().stream()
                .map(commentMapper::entityToDto)
                .toList();
    }
}
