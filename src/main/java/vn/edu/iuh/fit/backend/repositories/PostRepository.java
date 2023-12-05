package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.backend.models.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.published = true")
    public Page<Post> findAllByPublishedIsTrue(Pageable pageable);

    public List<Post> findAllByAuthor_IdAndPublishedIsTrue(long authorID);
}
