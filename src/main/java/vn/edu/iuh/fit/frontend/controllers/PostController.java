package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.Post;
import vn.edu.iuh.fit.backend.models.PostComment;
import vn.edu.iuh.fit.backend.models.User;
import vn.edu.iuh.fit.backend.repositories.PostCommentRepository;
import vn.edu.iuh.fit.backend.repositories.PostRepository;
import vn.edu.iuh.fit.backend.repositories.UserRepository;
import vn.edu.iuh.fit.backend.services.PostCommentService;
import vn.edu.iuh.fit.backend.services.PostService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/posts")
public class PostController {
    private PostRepository postRepository;
    private PostService postService;
    private PostCommentRepository postCommentRepository;
    private PostCommentService postCommentService;
    @Autowired
    public PostController(PostRepository postRepository, PostService postService, PostCommentRepository postCommentRepository, PostCommentService postCommentService) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.postCommentRepository = postCommentRepository;
        this.postCommentService = postCommentService;
    }

    @GetMapping("")
    public String showPostPaging(Model model, @RequestParam("page")Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size){
        int pageNo = page.orElse(1);
        int sizeNo = size.orElse(10);
        // only get posts is published
        Page<Post> postPage = postService.findAll(pageNo-1, sizeNo, "publishedAt", "desc");
        model.addAttribute("postPage", postPage);
        int totalsPage = postPage.getTotalPages();
        if (totalsPage > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalsPage).boxed().collect(Collectors.toList());;
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "posts/PostPaging";
    }

    @GetMapping("/add")
    public ModelAndView showAddPostPage(){
        ModelAndView modelAndView = new ModelAndView();

        Post post = new Post();

        modelAndView.addObject("post", post);
        modelAndView.setViewName("posts/AddNewPost");
        return modelAndView;
    }

    @PostMapping("/add")
    public String handleAddNewPost(@ModelAttribute("post") Post post
            , @RequestParam("parent") String parent
            , HttpServletRequest request, Model model){

        long parentId = -1;

        if (!parent.trim().isEmpty()){
            try {
                parentId = Long.parseLong(parent);
            } catch (Exception e){
                model.addAttribute("errPost", "Phải là số nguyên dương!");
                return "posts/AddNewPost";
            }

            Post objParentPost = postRepository.findById(parentId).orElse(null);

            if (objParentPost == null){
                model.addAttribute("errPost", "Không tồn tại Parent ID này!");
                return "posts/AddNewPost";
            }
            post.setParent(objParentPost);
        }

        post.setPublished(true);
        post.setPublishedAt(Instant.now());
        post.setCreateAt(Instant.now());
        post.setAuthor((User) request.getSession().getAttribute("account"));

        postRepository.save(post);
        return "redirect:/posts";
    }
    @GetMapping("/{id}")
    public ModelAndView openPostDetail(@PathVariable("id") long postID
            , @RequestParam("cmtPage") Optional<Integer> pageNo
            , @RequestParam("cmtSize") Optional<Integer> pageSize, ModelAndView modelAndView){

        Post post = postRepository.findById(postID).orElse(null);
        if (post == null || !post.getPublished()){
            return new ModelAndView("redirect:/");
        }

        int cmtPageNo = pageNo.orElse(1);
        int cmtPageSize = pageSize.orElse(10);

        Page<PostComment> postCommentPage = postCommentService.findAll(cmtPageNo-1, cmtPageSize
                , "publishedAt", "desc", postID);

        modelAndView.addObject("post", post);
        modelAndView.addObject("postCommentPage", postCommentPage);

        int totalPagesCmt = postCommentPage.getTotalPages();
        if (totalPagesCmt > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesCmt).boxed().collect(Collectors.toList());;
            modelAndView.addObject("pageCmtNumbers", pageNumbers);
        }



        modelAndView.setViewName("posts/PostDetail");
        return modelAndView;
    }
    @GetMapping("/my-post")
    public String openMyPosts(Model model, HttpServletRequest request){
        User account = (User)request.getSession().getAttribute("account");
        if (account == null){
            return "redirect:/";
        }
        List<Post> myPosts =
                postService.findAllBelongToMe(account.getId());
        model.addAttribute("posts", myPosts);
        return "posts/MyPost";
    }
    @GetMapping("/delete/{id}")
    public String handleDeletePost(@PathVariable("id") long postID){
        Post post = postService.hiddenPost(postID).orElse(null);
        if (post == null){
            return "redirect:/";
        }
        return "redirect:/posts/my-post";
    }
    @GetMapping("/edit/{id}")
    public ModelAndView openEditPost(@PathVariable("id") long postID, HttpServletRequest request, Model model){
        if (request.getSession() == null || request.getSession().getAttribute("account") == null){
            return new ModelAndView("redirect:/");
        }
        ModelAndView modelAndView = new ModelAndView();
        Post post = postRepository.findById(postID).orElse(null);
        if (post == null){
            return new ModelAndView("redirect:/posts");
        }
        modelAndView.addObject("post", post);
        modelAndView.setViewName("posts/EditPost");
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    public String handleUpdatePost(@PathVariable("id") long postID, @ModelAttribute("post") Post post, Model model){
        Post priviousPost = postRepository.findById(postID).orElse(null);

        if (post.getParent().getId() == null){
            post.setParent(null);
        } else {
            Post parent = postRepository.findById(post.getParent().getId()).orElse(null);
            if (parent == null){
                model.addAttribute("errEdit", "ParentID không tồn tại!");
                return "posts/EditPost";
            }
        }
        post.setUpdateAt(Instant.now());
        post.setCreateAt(priviousPost.getCreateAt());
        post.setAuthor(priviousPost.getAuthor());
        post.setPublishedAt(priviousPost.getPublishedAt());
        post.setPosts(priviousPost.getPosts());
        postRepository.save(post);
        return "redirect:/posts/my-post";
    }
}
