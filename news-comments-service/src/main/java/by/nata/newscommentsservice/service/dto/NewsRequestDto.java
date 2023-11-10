package by.nata.newscommentsservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * The {@code NewsRequestDto} record represents a data transfer object for creating news articles.
 *
 * <p>Properties:</p>
 * <p>- {@code title}: The title of the news article. It must not be null or empty.
 * <p>- {@code text}: The text content of the news article. It must not be null or empty.
 */
@Builder(setterPrefix = "with")
public record NewsRequestDto(
        @NotBlank(message = "Title must not be null or empty")
        String title,
        @NotBlank(message = "Text must not be null or empty")
        String text
) {
}
