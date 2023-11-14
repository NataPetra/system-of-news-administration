package by.nata.newscommentsservice.controller.api;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import by.nata.newscommentsservice.service.validator.NewsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The {@code NewsDocOpenApi} interface provides OpenAPI documentation for the News Controller,
 * which handles operations related to news in the application.
 */
@Tag(name = "News Controller", description = "API for working with news")
public interface NewsDocOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String FORBIDDEN = "FORBIDDEN";
    String NOT_FOUND = "NOT FOUND";
    String NO_CONTENT = "NO CONTENT";
    String OK = "OK";

    /**
     * Saves a new news section.
     *
     * @param request The request body for creating a new news section.
     * @return ResponseEntity<NewsResponseDto> with the created news section and HTTP status 201 (CREATED).
     */
    @Operation(
            summary = "Save news",
            description = "Creates and saves a new news section.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    ResponseEntity<NewsResponseDto> saveNews(
            @Parameter(description = "Request body containing news details",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NewsRequestDto.class)))
            @RequestBody @Valid NewsRequestDto request);

    /**
     * Updates an existing news section.
     *
     * @param id      The ID of the news section to be updated.
     * @param request The request body for updating an existing news section.
     * @return NewsResponseDto with the updated news section details.
     */
    @Operation(
            summary = "Update news",
            description = "Updates an existing news section.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class))),
                    @ApiResponse(responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)))
            }
    )
    NewsResponseDto updateNews(
            @Parameter(description = "ID of the news article to be updated")
            @PathVariable @NewsValidation Long id,
            @Parameter(description = "Request body containing updated news details",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NewsRequestDto.class)))
            @RequestBody @Valid NewsRequestDto request);

    /**
     * Retrieves a news section by its ID.
     *
     * @param id The ID of the news section to be retrieved.
     * @return NewsResponseDto with the details of the retrieved news section.
     */
    @Operation(
            summary = "Get news by ID",
            description = "Retrieves a news section by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            }
    )
    NewsResponseDto getNews(
            @Parameter(description = "ID of the news article to be retrieved")
            @PathVariable @NewsValidation Long id);

    /**
     * Retrieves all news sections.
     *
     * @param pageable The page number and size for pagination.
     * @return List<NewsResponseDto> containing all news sections.
     */
    @Operation(
            summary = "Get all news",
            description = "Retrieves all news sections.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class))))
            }
    )
    List<NewsResponseDto> getAllNews(
            @Parameter(description = "Page number and size for pagination")
            @PageableDefault(size = 5) @Nullable Pageable pageable);

    /**
     * Retrieves a news section with its associated comments by news ID.
     *
     * @param newsId   The ID of the news section for which comments are to be retrieved.
     * @param pageable The page number and size for pagination.
     * @return NewsWithCommentsResponseDto containing the news section and its associated comments.
     */
    @Operation(
            summary = "Get news with comments",
            description = "Retrieves a news section with its comments.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewsWithCommentsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            }
    )
    NewsWithCommentsResponseDto getNewsWithComments(
            @Parameter(description = "ID of the news article to be retrieved")
            @PathVariable @NewsValidation Long newsId,
            @Parameter(description = "Page number and size for pagination")
            @PageableDefault(size = 5) @Nullable Pageable pageable);

    /**
     * Searches for news sections based on a keyword and optional date filter.
     *
     * @param keyword    The keyword for searching news sections.
     * @param dateString The optional date filter for searching news sections.
     * @param pageable   The page number and size for paginated results.
     * @return List<NewsResponseDto> containing news sections matching the search criteria.
     */
    @Operation(
            summary = "Search news",
            description = "Searches for news based on a keyword and/or date.",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class))))
            }
    )
    List<NewsResponseDto> searchNews(
            @Parameter(description = "Keyword for searching news articles", example = "technology")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Date string for filtering news articles", example = "2023-01-01")
            @RequestParam(required = false) String dateString,
            @Parameter(description = "Page number and size for pagination")
            @PageableDefault(size = 5) @Nullable Pageable pageable);

    /**
     * Deletes a news section by its ID.
     *
     * @param id The ID of the news section to be deleted.
     * @return ResponseEntity<Void> with HTTP status 204 (NO CONTENT) indicating successful deletion.
     */
    @Operation(
            summary = "Delete news by ID",
            description = "Deletes a news section by its ID.",
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
    ResponseEntity<Void> deleteNews(
            @Parameter(description = "ID of the news article to be delete")
            @PathVariable @NewsValidation Long id);
}
