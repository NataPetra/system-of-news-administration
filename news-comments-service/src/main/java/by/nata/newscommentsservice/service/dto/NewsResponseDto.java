package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * The {@code NewsResponseDto} record represents a data transfer object for retrieving news article information.
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
