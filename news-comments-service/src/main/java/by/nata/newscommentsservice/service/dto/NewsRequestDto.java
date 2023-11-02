package by.nata.newscommentsservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record NewsRequestDto(
        @NotBlank(message = "Title must not be null or empty")
        String title,
        @NotBlank(message = "Text must not be null or empty")
        String text
) {
}
