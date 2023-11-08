package by.nata.newscommentsservice.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The {@code News} class is a JPA entity that represents news articles. It provides information about
 * the news article's title, content, creation timestamp, and associated comments.
 *
 * <p>Usage:</p>
 * <p>- Use this class to model and persist news articles in a database. It allows tracking the creation
 *   timestamp of news articles and managing associated comments.</p>
 *
 * <p>Fields:</p>
 * <p>- {@code id}: The unique identifier of the news article.</p>
 * <p>- {@code time}: The timestamp representing the news article's creation time.</p>
 * <p>- {@code title}: The title of the news article.</p>
 * <p>- {@code text}: The content of the news article.</p>
 * <p>- {@code comments}: A list of associated comments for the news article.</p>
 */
@Builder(setterPrefix = "with")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "news-service-db", name = "news")
public class News implements Serializable {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
