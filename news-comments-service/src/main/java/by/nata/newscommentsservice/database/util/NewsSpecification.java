package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.News;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * The {@code NewsSpecification} utility class provides a static method for creating a JPA Specification
 * to search for news articles that match specified criteria, including a keyword and a specific date range.
 * It is used for defining dynamic queries for the JPA repository.
 */
@UtilityClass
public class NewsSpecification {

    /**
     * Creates a JPA Specification for searching news articles using a keyword and a date range.
     * It performs case-insensitive searches in both the news article's title and content.
     *
     * @param keyword The keyword to search for in news articles.
     * @param date    The date within which news articles should fall.
     * @return A JPA Specification for searching news articles based on the provided criteria.
     */
    public static Specification<News> search(String keyword, Date date) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate finalPredicate = cb.conjunction();
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                finalPredicate = cb.or(
                        finalPredicate,
                        cb.or(
                                cb.like(cb.lower(root.get("title")), likePattern),
                                cb.like(cb.lower(root.get("text")), likePattern)
                        )
                );
            }
            if (Objects.nonNull(date)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Calendar nextDay = (Calendar) calendar.clone();
                nextDay.add(Calendar.DAY_OF_MONTH, 1);

                finalPredicate = cb.and(
                        finalPredicate,
                        cb.and(
                                cb.greaterThanOrEqualTo(root.get("time"), calendar.getTime()),
                                cb.lessThan(root.get("time"), nextDay.getTime())
                        )
                );
            }
            return finalPredicate;
        };
    }
}
