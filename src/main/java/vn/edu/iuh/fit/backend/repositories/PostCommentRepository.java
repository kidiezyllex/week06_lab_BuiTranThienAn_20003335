package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    public Page<PostComment> findPostCommentByPublishedIsTrueAndPostId(Pageable pageable, long postID);


}