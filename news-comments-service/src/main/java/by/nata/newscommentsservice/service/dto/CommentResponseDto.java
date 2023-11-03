package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CommentResponseDto(Long id, String text,
                                 String username, String time,
                                 Long newsId) {
}
