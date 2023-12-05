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
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "id", nullable = false)
    private Post post;
    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String title;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostComment> postComments;
    @Column(columnDefinition = "bit(1)", nullable = false)
    private Boolean published;
    @Column(columnDefinition = "text")
    private String content;
    @Column(name = "publishedAt", columnDefinition = "DATETIME(6)")
    private Instant publishedAt;
    @Column(name = "createAt",columnDefinition = "DATETIME(6)", nullable = false)
    private Instant createAt;
    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private PostComment parent;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;

    public PostComment(Post post, String title, Boolean published, String content, Instant publishedAt, Instant createAt, PostComment parent, User user) {
        this.post = post;
        this.title = title;
        this.published = published;
        this.content = content;
        this.publishedAt = publishedAt;
        this.createAt = createAt;
        this.parent = parent;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", post=" + post +
                ", title='" + title + '\'' +
                ", published=" + published +
                ", content='" + content + '\'' +
                ", publishedAt=" + publishedAt +
                ", createAt=" + createAt +
                ", parent=" + parent +
                ", user=" + user +
                '}';
    }
}
