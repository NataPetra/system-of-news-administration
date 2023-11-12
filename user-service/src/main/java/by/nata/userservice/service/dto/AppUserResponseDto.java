package by.nata.userservice.service.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record AppUserResponseDto(String username, String role) implements Serializable {
}
