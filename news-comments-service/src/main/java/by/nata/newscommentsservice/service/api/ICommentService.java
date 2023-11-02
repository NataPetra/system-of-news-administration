package by.nata.newscommentsservice.service.api;

import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;

import java.util.List;

public interface ICommentService {

    CommentResponseDto save(CommentRequestDto comment);

    CommentResponseDto update(Long id, CommentRequestDto comment);

    CommentResponseDto getCommentById(Long id);

    List<CommentResponseDto> findByNewsIdOrderByTimeDesc(Long newsId, int pageNumber, int pageSize);

    List<CommentResponseDto> findAllByNewsId(Long newsId);

    void delete(Long id);

    List<CommentResponseDto> searchComment(String keyword, int pageNumber, int pageSize);
}
