package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.util.Date;

@Builder(setterPrefix = "with")
public record NewsResponseDto(Long id, Date time,
                              String title, String text) {
}
