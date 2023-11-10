package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.Comment;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

/**
 * The {@code CommentSpecification} utility class provides a static method for creating a JPA Specification
 * to search for comments that match a given keyword. It is used for defining dynamic queries for the JPA repository.
 */
@UtilityClass
public class CommentSpecification {

    /**
     * Creates a JPA Specification for searching comments by a keyword. It performs a case-insensitive search
     * in both the comment text and the author's username.
     *
     * @param keyword The keyword to search for in comments.
     * @return A JPA Specification for searching comments that match the provided keyword.
     */
    public static Specification<Comment> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likePattern)
            );
        };
    }
}
