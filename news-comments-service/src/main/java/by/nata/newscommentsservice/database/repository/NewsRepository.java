package by.nata.newscommentsservice.database.repository;

import by.nata.newscommentsservice.database.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
