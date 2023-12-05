package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.Post;
import vn.edu.iuh.fit.backend.models.PostComment;
import vn.edu.iuh.fit.backend.models.User;
import vn.edu.iuh.fit.backend.repositories.PostCommentRepository;
import vn.edu.iuh.fit.backend.services.PostCommentService;

import java.time.Instant;
import java.util.Optional;

@Controller
@RequestMapping("/post-comments")
public class PostCommentController {
    private PostCommentService postCommentService;
    private PostCommentRepository postCommentRepository;
    @Autowired
    public PostCommentController(PostCommentService postCommentService, PostCommentRepository postCommentRepository) {
        this.postCommentService = postCommentService;
        this.postCommentRepository = postCommentRepository;
    }

    @PostMapping("/add")
    public ModelAndView handleAddNewPostComment( @RequestParam("postID") long postID
            , @RequestParam("parentCmtID") String commentIDStr, @RequestParam("title") String title
    , @RequestParam("content")String content, HttpServletRequest request){
        PostComment postComment  = new PostComment();
        ModelAndView modelAndView = new ModelAndView();
        if (!commentIDStr.trim().isEmpty()){
            long parentCommnentID = -1;
            try {
                parentCommnentID = Long.parseLong(commentIDStr);
            } catch (Exception e){
                modelAndView.addObject("errAddComment", "Parent Comment ID không hợp lệ!");
                modelAndView.setViewName("redirect:/posts/" + postID);
                return modelAndView;
            }
            PostComment parentPostComment = postCommentRepository.findById(parentCommnentID).orElse(null);
            if (parentPostComment == null || !parentPostComment.getPublished()
                    || parentPostComment.getPost().getId() != postID){
                modelAndView.addObject("errAddComment", "Parent Comment ID không tồn tại!");
                modelAndView.setViewName("redirect:/posts/" + postID);
                return modelAndView;
            }
            postComment.setParent(parentPostComment);
        }


        postComment.setPost(new Post(postID));
        postComment.setPublished(true);
        postComment.setContent(content);
        postComment.setTitle(title);
        postComment.setCreateAt(Instant.now());
        postComment.setPublishedAt(Instant.now());
        postComment.setUser((User) request.getSession().getAttribute("account"));
        postCommentRepository.save(postComment);
        modelAndView.setViewName("redirect:/posts/" + postID);
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public String handleDeleteComment(@PathVariable("id") long commentID){
        PostComment postComment = postCommentService.hiddenComment(commentID).orElse(null);
        if (postComment == null){
            return "redirect:/";
        }
        return "redirect:/posts/" + postComment.getPost().getId();
    }
    @GetMapping("/edit/{id}")
    public ModelAndView modelAndView(@PathVariable("id") long commentID, HttpServletRequest request) {
        if (request.getSession() == null || request.getSession().getAttribute("account") == null){
            return new ModelAndView("redirect:/posts");
        }
        ModelAndView modelAndView = new ModelAndView();
        PostComment postComment = postCommentRepository.findById(commentID).orElse(null);
        if (postComment == null) {
            return new ModelAndView("redirect:/");
        }
        modelAndView.addObject("postComment", postComment);
        modelAndView.setViewName("post_comments/editPostComment");
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    public String handleUpdatePostComment(@PathVariable("id") long commentID
            , @ModelAttribute("postComment") PostComment postComment
            , HttpServletRequest request){
        if (request.getSession() == null || request.getSession().getAttribute("account") == null){
            return"redirect:/posts";
        }

        PostComment priviousPostComment = postCommentRepository.findById(commentID).orElse(null);


        System.out.println(postComment);
        if (postComment.getParent().getId() == null){
            postComment.setParent(null);
        }
        postComment.setCreateAt(priviousPostComment.getCreateAt());
        postComment.setPublishedAt(priviousPostComment.getPublishedAt());
        postComment.setPostComments(priviousPostComment.getPostComments());
        postComment.setUser((User) request.getSession().getAttribute("account"));
        postCommentRepository.save(priviousPostComment);
        postCommentRepository.save(postComment);
        return "redirect:/posts/" + postComment.getPost().getId();
    }
}
