package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.util.List;

/**
 * The {@code NewsWithCommentsResponseDto} record represents a data transfer object for retrieving news articles with associated comments.
 *
 * <p>Properties:</p>
 * <p>- {@code id}: The unique identifier of the news article.
 * <p>- {@code time}: The creation time of the news article.
 * <p>- {@code title}: The title of the news article.
 * <p>- {@code text}: The text content of the news article.
 * <p>- {@code commentsList}: A list of associated comments represented as {@link CommentResponseDto}.
 *
 * @see CommentResponseDto
 */
@Builder(setterPrefix = "with")
public record NewsWithCommentsResponseDto(Long id, String time,
                                          String title, String text,
                                          List<CommentResponseDto> commentsList) {
}
