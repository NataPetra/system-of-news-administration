package by.nata.newscommentsservice.service.dto;

import by.nata.newscommentsservice.service.validator.NewsValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record CommentRequestDto(
        @NotBlank(message = "Text must not be null or empty")
        String text,
        @NotBlank(message = "Username must not be null or empty")
        String username,
        @NotNull(message = "News id must not be null")
        @NewsValidation
        Long newsId) {
}
