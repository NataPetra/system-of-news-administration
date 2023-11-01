package by.nata.newscommentsservice.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record CommentRequestDto(
        @NotNull
        String text,
        @NotNull
        String username,
        @NotNull
        Long newsId) {
}
