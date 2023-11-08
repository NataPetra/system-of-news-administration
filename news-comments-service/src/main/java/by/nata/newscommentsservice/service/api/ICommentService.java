package by.nata.newscommentsservice.service.api;

import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;

import java.util.List;

/**
 * The {@code ICommentService} interface defines the contract for the service layer to manage comment-related operations.
 *
 * <p>Usage:</p>
 * <p>- Implement this interface to provide business logic and functionality for managing comments, including saving,
 *   updating, retrieving, and deleting comments, as well as searching for comments and checking the existence of a comment.</p>
 *
 * <p>Methods:</p>
 * <p>- {@code save}: Saves a new comment based on the provided {@link CommentRequestDto}.
 * <p>- {@code update}: Updates an existing comment with the specified ID using the provided {@link CommentRequestDto}.
 * <p>- {@code getCommentById}: Retrieves a comment by its unique identifier.
 * <p>- {@code findByNewsIdOrderByTimeDesc}: Retrieves a list of comments for a specific news article, ordered by time in descending order.
 * <p>- {@code findAllByNewsId}: Retrieves a list of comments for a specific news article.
 * <p>- {@code delete}: Deletes a comment with the specified ID.
 * <p>- {@code searchComment}: Searches for comments based on a keyword, with pagination support.
 * <p>- {@code isCommentExist}: Checks whether a comment with a specific ID exists.
 *
 * @see CommentRequestDto
 * @see CommentResponseDto
 */
public interface ICommentService {

    /**
     * Saves a new comment based on the provided {@link CommentRequestDto}.
     *
     * @param comment The {@link CommentRequestDto} containing the comment data to be saved.
     * @return A {@link CommentResponseDto} representing the saved comment.
     */
    CommentResponseDto save(CommentRequestDto comment);

    /**
     * Updates an existing comment with the specified ID using the provided {@link CommentRequestDto}.
     *
     * @param id      The unique identifier of the comment to be updated.
     * @param comment The {@link CommentRequestDto} containing the updated comment data.
     * @return A {@link CommentResponseDto} representing the updated comment.
     */
    CommentResponseDto update(Long id, CommentRequestDto comment);

    /**
     * Retrieves a comment by its unique identifier.
     *
     * @param id The unique identifier of the comment to retrieve.
     * @return A {@link CommentResponseDto} representing the retrieved comment.
     */
    CommentResponseDto getCommentById(Long id);

    /**
     * Retrieves a list of comments for a specific news article, ordered by time in descending order.
     *
     * @param newsId    The unique identifier of the news article.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The number of comments per page.
     * @return A list of {@link CommentResponseDto} representing the comments for the specified news article.
     */
    List<CommentResponseDto> findByNewsIdOrderByTimeDesc(Long newsId, int pageNumber, int pageSize);

    /**
     * Retrieves a list of comments for a specific news article.
     *
     * @param newsId The unique identifier of the news article.
     * @return A list of {@link CommentResponseDto} representing the comments for the specified news article.
     */
    List<CommentResponseDto> findAllByNewsId(Long newsId);

    /**
     * Deletes a comment with the specified ID.
     *
     * @param id The unique identifier of the comment to be deleted.
     */
    void delete(Long id);

    /**
     * Searches for comments based on a keyword, with pagination support.
     *
     * @param keyword    The keyword to search for in comments.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The number of comments per page.
     * @return A list of {@link CommentResponseDto} representing the comments that match the provided keyword.
     */
    List<CommentResponseDto> searchComment(String keyword, int pageNumber, int pageSize);

    /**
     * Checks whether a comment with a specific ID exists.
     *
     * @param id The unique identifier of the comment to check for existence.
     * @return {@code true} if the comment exists, {@code false} otherwise.
     */
    boolean isCommentExist(Long id);
}
