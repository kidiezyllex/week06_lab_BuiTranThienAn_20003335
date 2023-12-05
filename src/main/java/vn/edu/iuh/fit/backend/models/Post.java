package vn.edu.iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "bit(1)", nullable = false)
    private Boolean published;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private Post parent;
    @Column(name = "metaTitle", columnDefinition = "VARCHAR(100)")
    private String metaTitle;
    @Column(columnDefinition = "tinytext")
    private String summary;
    @Column(name = "createAt", columnDefinition = "DATETIME(6)", nullable = false)
    private Instant createAt;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts;
    @Column(columnDefinition = "VARCHAR(75)", nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id", nullable = false)
    private User author;
    @Column(name = "updateAt", columnDefinition = "DATETIME(6)")
    private Instant updateAt;
    @Column(name = "publishedAt",columnDefinition = "DATETIME(6)")
    private Instant publishedAt;

    public Post(Long id) {
        this.id = id;
    }

    public Post(Boolean published, String content, Post parent, String metaTitle, String summary, Instant createAt, Set<Post> posts, String title, User author, Instant updateAt, Instant publishedAt) {
        this.published = published;
        this.content = content;
        this.parent = parent;
        this.metaTitle = metaTitle;
        this.summary = summary;
        this.createAt = createAt;
        this.posts = posts;
        this.title = title;
        this.author = author;
        this.updateAt = updateAt;
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", published=" + published +
                ", content='" + content + '\'' +
                ", parent=" + parent +
                ", metaTitle='" + metaTitle + '\'' +
                ", summary='" + summary + '\'' +
                ", createAt=" + createAt +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", updateAt=" + updateAt +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
