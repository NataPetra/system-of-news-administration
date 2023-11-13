package by.nata.userservice.controller.api;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.userservice.service.dto.AppUserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API for user registration.
 * - Tags: Registration Controller
 */
@Tag(name = "Registration Controller", description = "API for user registration")
public interface RegistrationDocOpenApi {

    String CREATED = "CREATED";
    String BAD_REQUEST = "BAD REQUEST";

    /**
     * Register a user with the "ADMIN" role.
     *
     * @param request User details for registration.
     * @return ResponseEntity with the registered username and HTTP 201 status.
     */
    @Operation(
            summary = "Register administrator",
            description = "Registers a new administrator.",
            responses = {
                    @ApiResponse(responseCode = "201", description = CREATED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<String> registerAdministrator(
            @Parameter(name = "request", description = "The user registration request", required = true)
            @RequestBody @Valid AppUserRequestDto request);

    /**
     * Register a user with the "JOURNALIST" role.
     *
     * @param request User details for registration.
     * @return ResponseEntity with the registered username and HTTP 201 status.
     */
    @Operation(
            summary = "Register journalist",
            description = "Registers a new journalist.",
            responses = {
                    @ApiResponse(responseCode = "201", description = CREATED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<String> registerJournalist(
            @Parameter(name = "request", description = "The user registration request", required = true)
            @RequestBody @Valid AppUserRequestDto request);

    /**
     * Register a user with the "SUBSCRIBER" role.
     *
     * @param request User details for registration.
     * @return ResponseEntity with the registered username and HTTP 201 status.
     */
    @Operation(
            summary = "Register subscriber",
            description = "Registers a new subscriber.",
            responses = {
                    @ApiResponse(responseCode = "201", description = CREATED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<String> registerSubscriber(
            @Parameter(name = "request", description = "The user registration request", required = true)
            @RequestBody @Valid AppUserRequestDto request);
}
