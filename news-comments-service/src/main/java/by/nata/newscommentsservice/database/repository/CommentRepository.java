package by.nata.newscommentsservice.database.repository;

import by.nata.newscommentsservice.database.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
