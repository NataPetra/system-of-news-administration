package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * The {@code CommentResponseDto} record represents a data transfer object for retrieving comment information.
 *
 * <p>Usage:</p>
 * <p>- Use this data transfer object to retrieve comment details, such as the comment's unique identifier, text content, author's username, creation time, and associated news article ID.</p>
 *
 * <p>Properties:</p>
 * <p>- {@code id}: The unique identifier of the comment.
 * <p>- {@code text}: The text content of the comment.
 * <p>- {@code username}: The username of the comment author.
 * <p>- {@code time}: The creation time of the comment.
 * <p>- {@code newsId}: The unique identifier of the associated news article.
 */
@Builder(setterPrefix = "with")
public record CommentResponseDto(Long id, String text,
                                 String username, String time,
                                 Long newsId) implements Serializable {
}
