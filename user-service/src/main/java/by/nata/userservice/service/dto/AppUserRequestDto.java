package by.nata.userservice.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

/**
 * The {@code AppUserRequestDto} record represents the data transfer object (DTO) for creating or updating an application user.
 * <p>
 * - Record Fields:
 *   - {@code username}: The username of the user.
 *   - {@code password}: The password of the user.
 */
@Builder(setterPrefix = "with")
public record AppUserRequestDto(
        @NotBlank(message = "Username should be empty")
        @Length(max = 40, message = "Username should be no longer than {max} characters")
        @Schema(description = "Username", example = "admin")
        String username,
        @NotBlank(message = "Password should be empty")
        @Length(max = 72, message = "Password's length should be no longer than {max} characters")
        @Schema(description = "Password", example = "admin")
        String password
) {
}
