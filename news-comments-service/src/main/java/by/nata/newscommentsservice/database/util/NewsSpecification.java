package by.nata.newscommentsservice.database.util;

import by.nata.newscommentsservice.database.model.News;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class NewsSpecification {

    public static Specification<News> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(keyword)) {
                return null;
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), likePattern),
                    criteriaBuilder.like(root.get("text"), likePattern)
            );
        };
    }

    public static Specification<News> newsCreatedOnDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar nextDay = (Calendar) calendar.clone();
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        return (root, query, builder) -> builder.and(
                builder.greaterThanOrEqualTo(root.get("time"), calendar.getTime()),
                builder.lessThan(root.get("time"), nextDay.getTime())
        );
    }
}
