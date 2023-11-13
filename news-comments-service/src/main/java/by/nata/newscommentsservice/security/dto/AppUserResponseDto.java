package by.nata.newscommentsservice.security.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * The {@code AppUserResponseDto} class represents a data transfer object (DTO) for user responses.
 */
@Builder(setterPrefix = "with")
public record AppUserResponseDto(String username, String role) implements Serializable {
}
