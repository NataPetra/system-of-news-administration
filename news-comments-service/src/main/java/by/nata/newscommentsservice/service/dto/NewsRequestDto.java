package by.nata.newscommentsservice.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

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
        @Schema(description = "Title of the news", defaultValue = "Default title")
        String title,
        @NotBlank(message = "Text must not be null or empty")
        @Schema(description = "Text of the news", defaultValue = "Default text")
        String text
) implements Serializable {
}
