package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record CommentResponseDto(Long id, String text,
                                 String username, String time,
                                 Long newsId) implements Serializable {
}
