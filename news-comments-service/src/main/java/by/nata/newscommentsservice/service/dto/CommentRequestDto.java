package by.nata.newscommentsservice.service.dto;

import by.nata.newscommentsservice.service.validator.NewsValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

/**
 * The {@code CommentRequestDto} record represents the data transfer object for creating or updating comments.
 *
 * <p>Properties:</p>
 * <p>- {@code text}: The text content of the comment. It must not be null or empty.
 * <p>- {@code username}: The username of the comment author. It must not be null or empty.
 * <p>- {@code newsId}: The unique identifier of the associated news article. It must not be null and should be validated using the {@link NewsValidation} constraint.
 *
 * @see NewsValidation
 */
@Builder(setterPrefix = "with")
public record CommentRequestDto(
        @NotBlank(message = "Text must not be null or empty")
        String text,
        @NotBlank(message = "Username must not be null or empty")
        @Length(max = 40, message = "Username should be no longer than {max} characters")
        String username,
        @NotNull(message = "News id must not be null")
        @NewsValidation
        Long newsId) {
}
