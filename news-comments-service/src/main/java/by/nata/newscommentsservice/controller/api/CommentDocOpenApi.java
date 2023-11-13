package by.nata.newscommentsservice.controller.api;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.validator.CommentValidation;
import by.nata.newscommentsservice.service.validator.NewsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The {@code CommentDocOpenApi} interface provides OpenAPI documentation for the Comment Controller,
 * which handles operations related to comments in the application.
 */
@Tag(name = "Comment Controller", description = "API for working with comments")
public interface CommentDocOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String FORBIDDEN = "FORBIDDEN";
    String NOT_FOUND = "NOT FOUND";
    String NO_CONTENT = "NO CONTENT";
    String OK = "OK";

    /**
     * Saves a new comment.
     *
     * @param request The request body for creating a new comment.
     * @return ResponseEntity<CommentResponseDto> with the created comment and HTTP status 201 (CREATED).
     */
    @Operation(
            summary = "Save comment",
            description = "Creates and saves a new comment.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<CommentResponseDto> saveComment(
            @Parameter(description = "The request body for creating a new comment",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentRequestDto.class)))
            @RequestBody @Valid CommentRequestDto request);

    /**
     * Updates an existing comment.
     *
     * @param id      The ID of the comment to be updated.
     * @param request The request body for updating an existing comment.
     * @return CommentResponseDto with the updated comment details.
     */
    @Operation(
            summary = "Update comment",
            description = "Updates an existing comment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    CommentResponseDto updateComment(
            @Parameter(description = "The ID of the comment to be updated")
            @PathVariable @CommentValidation Long id,
            @Parameter(description = "The request body for updating an existing comment",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentRequestDto.class)))
            @RequestBody @Valid CommentRequestDto request);

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to be retrieved.
     * @return CommentResponseDto with the details of the retrieved comment.
     */
    @Operation(
            summary = "Get comment by ID",
            description = "Retrieves a comment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            }
    )
    CommentResponseDto getComment(
            @Parameter(description = "The ID of the comment to be get")
            @PathVariable @CommentValidation Long id);

    /**
     * Retrieves all comments for a given news ID.
     *
     * @param newsId The ID of the news for which comments are to be retrieved.
     * @return List<CommentResponseDto> containing comments for the specified news.
     */
    @Operation(
            summary = "Get comments by news ID",
            description = "Retrieves all comments for a given news ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))))
            }
    )
    List<CommentResponseDto> getCommentsByNewsId(
            @Parameter(description = "The ID of the news to be get with comments")
            @PathVariable @NewsValidation Long newsId);

    /**
     * Searches for comments based on a keyword.
     *
     * @param keyword    The keyword for searching comments.
     * @param pageNumber The page number for paginated results.
     * @param pageSize   The page size for paginated results.
     * @return List<CommentResponseDto> containing comments matching the search criteria.
     */
    @Operation(
            summary = "Search comments",
            description = "Searches for comments based on a keyword.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))))
            }
    )
    List<CommentResponseDto> searchComments(
            @Parameter(description = "The keyword for searching comments")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "The page number for paginated results")
            @RequestParam int pageNumber,
            @Parameter(description = "The page size for paginated results")
            @RequestParam int pageSize);

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to be deleted.
     * @return ResponseEntity<Void> with HTTP status 204 (NO CONTENT) indicating successful deletion.
     */
    @Operation(
            summary = "Delete comment by ID",
            description = "Deletes a comment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = NO_CONTENT),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "The ID of the comment to be delete")
            @PathVariable @CommentValidation Long id);
}
