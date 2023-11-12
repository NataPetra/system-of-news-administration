package by.nata.newscommentsservice.database.repository;

import by.nata.newscommentsservice.database.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * The {@code CommentRepository} interface is a Spring Data JPA repository that provides methods for
 * interacting with the database to perform CRUD (Create, Read, Update, Delete) operations for comment
 * entities. It extends the {@link JpaRepository} and {@link JpaSpecificationExecutor} interfaces.
 *
 * <p>Usage:</p>
 * <p>- Use this repository to perform database operations on {@link Comment} entities, such as saving,
 *   retrieving, updating, or deleting comments.</p>
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    /**
     * Retrieves a page of comments for a specific news article, ordered by creation time in descending order.
     *
     * @param newsId    The unique identifier of the news article.
     * @param pageable  A {@link Pageable} object specifying the page and sorting options.
     * @return A page of comments for the specified news article.
     */
    Page<Comment> findByNewsIdOrderByTimeDesc(Long newsId, Pageable pageable);

    /**
     * Retrieves a list of comments for a specific news article.
     *
     * @param newsId The unique identifier of the news article.
     * @return A list of comments associated with the specified news article.
     */
    List<Comment> findAllByNewsId(Long newsId);
}
