package by.nata.newscommentsservice.service.api;

import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The {@code INewsService} interface defines the contract for the service layer to manage news-related operations.
 *
 * <p>Usage:</p>
 * <p>- Implement this interface to provide business logic and functionality for managing news articles, including saving,
 *   updating, retrieving, and deleting news articles, as well as searching for news articles and checking the existence of a news article.</p>
 *
 * @see NewsRequestDto
 * @see NewsResponseDto
 * @see NewsWithCommentsResponseDto
 */
public interface INewsService {

    /**
     * Saves a new news article based on the provided {@link NewsRequestDto}.
     *
     * @param news The {@link NewsRequestDto} containing the news article data to be saved.
     * @return A {@link NewsResponseDto} representing the saved news article.
     */
    NewsResponseDto save(NewsRequestDto news);

    /**
     * Updates an existing news article with the specified ID using the provided {@link NewsRequestDto}.
     *
     * @param id   The unique identifier of the news article to be updated.
     * @param news The {@link NewsRequestDto} containing the updated news article data.
     * @return A {@link NewsResponseDto} representing the updated news article.
     */
    NewsResponseDto update(Long id, NewsRequestDto news);

    /**
     * Retrieves a news article by its unique identifier.
     *
     * @param id The unique identifier of the news article to retrieve.
     * @return A {@link NewsResponseDto} representing the retrieved news article.
     */
    NewsResponseDto getNewsById(Long id);

    /**
     * Retrieves a list of all news articles with pagination support.
     *
     * @param pageable The page number and size for pagination.
     * @return A list of {@link NewsResponseDto} representing news articles.
     */
    List<NewsResponseDto> getAllNews(Pageable pageable);

    /**
     * Retrieves a news article along with its associated comments, with pagination support.
     *
     * @param newsId    The unique identifier of the news article to retrieve.
     * @param pageable The page number and size for pagination.
     * @return A {@link NewsWithCommentsResponseDto} representing the news article and its associated comments.
     */
    NewsWithCommentsResponseDto getNewsWithComments(Long newsId, Pageable pageable);

    /**
     * Deletes a news article with the specified ID.
     *
     * @param id The unique identifier of the news article to be deleted.
     */
    void delete(Long id);

    /**
     * Searches for news articles based on a keyword, date, and pagination parameters.
     *
     * @param keyword    The keyword to search for in news articles.
     * @param dateString The date string to filter news articles.
     * @param pageable The page number and size for pagination.
     * @return A list of {@link NewsResponseDto} representing the news articles that match the provided criteria.
     */
    List<NewsResponseDto> searchNews(String keyword, String dateString, Pageable pageable);

    /**
     * Checks whether a news article with a specific ID exists.
     *
     * @param id The unique identifier of the news article to check for existence.
     * @return {@code true} if the news article exists, {@code false} otherwise.
     */
    boolean isNewsExist(Long id);
}
