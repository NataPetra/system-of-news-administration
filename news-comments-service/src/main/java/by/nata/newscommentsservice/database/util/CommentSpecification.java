package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.Comment;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CommentSpecification {
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
