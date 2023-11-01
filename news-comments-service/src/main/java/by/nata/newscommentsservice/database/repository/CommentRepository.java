package by.nata.newscommentsservice.database.repository;

import by.nata.newscommentsservice.database.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByNewsIdOrderByTimeDesc(Long newsId, Pageable pageable);

    List<Comment> findAllByNewsId(Long newsId);
}
