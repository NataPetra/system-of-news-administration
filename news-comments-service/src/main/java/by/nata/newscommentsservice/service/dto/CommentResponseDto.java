package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.util.Date;

@Builder(setterPrefix = "with")
public record CommentResponseDto(Long id, String text,
                                 String username, Date time,
                                 Long newsId) {
}
