package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder(setterPrefix = "with")
public record NewsWithCommentsResponseDto(Long id, Date time,
                                          String title, String text,
                                          List<CommentResponseDto> commentsList) {
}
