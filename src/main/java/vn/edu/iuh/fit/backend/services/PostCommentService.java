package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.models.PostComment;
import vn.edu.iuh.fit.backend.repositories.PostCommentRepository;

import java.util.Optional;

@Service
public class PostCommentService {
    private PostCommentRepository postCommentRepository;
    @Autowired
    public PostCommentService(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }
    public Optional<PostComment> hiddenComment(long commentID){
        PostComment postComment = postCommentRepository.findById(commentID).orElse(null);
        if (postComment == null){
            return Optional.empty();
        }
        postComment.setPublished(false);
        postCommentRepository.save(postComment);
        return Optional.of(postComment);

    }
    public Page<PostComment> findAll(int pageNo, int pageSize, String sortBy, String sortDirection, long postID){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return postCommentRepository.findPostCommentByPublishedIsTrueAndPostId(pageable, postID);
    }
}
