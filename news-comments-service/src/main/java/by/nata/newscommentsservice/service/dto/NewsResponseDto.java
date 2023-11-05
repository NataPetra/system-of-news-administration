package by.nata.newscommentsservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record NewsResponseDto(Long id, String time,
                              String title, String text) implements Serializable {
}
