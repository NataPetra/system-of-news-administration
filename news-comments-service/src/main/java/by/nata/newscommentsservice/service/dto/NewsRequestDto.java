package by.nata.newscommentsservice.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record NewsRequestDto(
        @NotNull(message = "Title must not be null")
        String title,
        @NotNull(message = "Text must not be null")
        String text
) {
}
