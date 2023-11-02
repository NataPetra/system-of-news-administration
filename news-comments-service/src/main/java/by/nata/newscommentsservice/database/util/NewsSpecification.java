package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.News;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@UtilityClass
public class NewsSpecification {

    public static Specification<News> search(String keyword, Date date) {
        return (root, query, criteriaBuilder) -> {
            Specification<News> specification = Specification.where(null);

            if (!StringUtils.isEmpty(keyword)) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                specification = specification.and((root1, query1, cb) ->
                        cb.or(
                                cb.like(root1.get("title"), likePattern),
                                cb.like(root1.get("text"), likePattern)
                        ));
            }

            if (Objects.nonNull(date)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Calendar nextDay = (Calendar) calendar.clone();
                nextDay.add(Calendar.DAY_OF_MONTH, 1);

                specification = specification.and((root1, query1, cb) ->
                        cb.and(
                                cb.greaterThanOrEqualTo(root1.get("time"), calendar.getTime()),
                                cb.lessThan(root1.get("time"), nextDay.getTime())
                        ));
            }

            return (Predicate) specification;
        };
    }

//
//    public static Specification<News> search(String keyword) {
//        return (root, query, criteriaBuilder) -> {
//            String likePattern = "%" + keyword + "%";
//            return criteriaBuilder.or(
//                    criteriaBuilder.like(root.get("title"), likePattern),
//                    criteriaBuilder.like(root.get("text"), likePattern)
//            );
//        };
//    }
//
//    public static Specification<News> newsCreatedOnDate(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        Calendar nextDay = (Calendar) calendar.clone();
//        nextDay.add(Calendar.DAY_OF_MONTH, 1);
//
//        return (root, query, builder) -> builder.and(
//                builder.greaterThanOrEqualTo(root.get("time"), calendar.getTime()),
//                builder.lessThan(root.get("time"), nextDay.getTime())
//        );
//    }
}
