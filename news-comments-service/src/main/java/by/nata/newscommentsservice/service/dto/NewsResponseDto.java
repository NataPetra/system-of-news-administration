package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record NewsResponseDto(Long id, String time,
                              String title, String text) {
}
