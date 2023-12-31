package by.nata.newscommentsservice.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

/**
 * The {@code Comment} class is a JPA entity that represents a comment associated with news articles.
 * It provides information about the comment's content, author, creation timestamp, and the associated news article.
 *
 * <p>Fields:</p>
 * <p>- {@code id}: The unique identifier of the comment.</p>
 * <p>- {@code text}: The content of the comment.</p>
 * <p>- {@code username}: The author's username who posted the comment.</p>
 * <p>- {@code time}: The timestamp representing the comment's creation time.</p>
 * <p>- {@code news}: The associated news article to which the comment belongs.</p>
 */
@Builder(setterPrefix = "with")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "news-service-db", name = "comments")
public class Comment implements Serializable {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, length = 40)
    private String username;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date time;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

}






