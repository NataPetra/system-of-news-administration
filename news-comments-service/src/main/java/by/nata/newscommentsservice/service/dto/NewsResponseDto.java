package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * The {@code NewsResponseDto} record represents a data transfer object for retrieving news article information.
 *
 * <p>Usage:</p>
 * <p>- Use this data transfer object to retrieve news article details, such as the article's unique identifier, creation time, title, and text content.</p>
 *
 * <p>Properties:</p>
 * <p>- {@code id}: The unique identifier of the news article.
 * <p>- {@code time}: The creation time of the news article.
 * <p>- {@code title}: The title of the news article.
 * <p>- {@code text}: The text content of the news article.
 */
@Builder(setterPrefix = "with")
public record NewsResponseDto(Long id, String time,
                              String title, String text) implements Serializable {
}
