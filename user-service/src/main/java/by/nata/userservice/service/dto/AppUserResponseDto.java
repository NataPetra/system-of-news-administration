package by.nata.userservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * The {@code AppUserResponseDto} record represents the data transfer object (DTO) for providing user information in responses.
 * <p>
 * - Record Fields:
 *   - {@code username}: The username of the user.
 *   - {@code role}: The role of the user.
 */
@Builder(setterPrefix = "with")
public record AppUserResponseDto(String username, String role) implements Serializable {
}
