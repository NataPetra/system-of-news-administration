package by.nata.userservice.controller.api;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.userservice.service.dto.AppUserRequestDto;
import by.nata.userservice.service.dto.AppUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication Controller", description = "API for user authentication")
public interface AuthenticationDocOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String OK = "OK";

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and returns an access token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<String> authenticate(
            @Parameter(description = "User credentials", required = true)
            @RequestBody @Valid AppUserRequestDto request);

    @Operation(
            summary = "Validate access token",
            description = "Validates the provided access token and returns user details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AppUserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    AppUserResponseDto validate(
            @Parameter(description = "Bearer token", example = "<token>")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader);
}