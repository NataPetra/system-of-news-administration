package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
public record NewsWithCommentsResponseDto(Long id, String time,
                                          String title, String text,
                                          List<CommentResponseDto> commentsList) {
}
