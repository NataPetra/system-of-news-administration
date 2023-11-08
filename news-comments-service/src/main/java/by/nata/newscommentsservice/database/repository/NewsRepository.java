package by.nata.newscommentsservice.database.repository;

import by.nata.newscommentsservice.database.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code NewsRepository} interface is a Spring Data JPA repository that provides methods for
 * interacting with the database to perform CRUD (Create, Read, Update, Delete) operations for news
 * article entities. It extends the {@link JpaRepository} and {@link JpaSpecificationExecutor} interfaces.
 *
 * <p>Usage:</p>
 * <p>- Use this repository to perform database operations on {@link News} entities, such as saving,
 *   retrieving, updating, or deleting news articles.</p>
 */
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}
