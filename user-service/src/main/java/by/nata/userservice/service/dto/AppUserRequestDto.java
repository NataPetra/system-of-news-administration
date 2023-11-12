package by.nata.userservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder(setterPrefix = "with")
public record AppUserRequestDto(
        @NotBlank(message = "Username should be empty")
        @Length(max = 40, message = "Username should be no longer than {max} characters")
        String username,
        @NotBlank(message = "Password should be empty")
        @Length(max = 72, message = "Password's length should be no longer than {max} characters")
        String password
) {
}
