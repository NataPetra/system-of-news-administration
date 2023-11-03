package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.News;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@UtilityClass
public class NewsSpecification {

    public static Specification<News> search(String keyword, Date date) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate finalPredicate = cb.conjunction();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                finalPredicate = cb.or(
                        finalPredicate,
                        cb.or(
                                cb.like(root.get("title"), likePattern),
                                cb.like(root.get("text"), likePattern)
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
