package vn.edu.iuh.fit.frontend.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.User;
import vn.edu.iuh.fit.backend.repositories.UserRepository;

import java.time.Instant;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/sign-up")
    public ModelAndView openSignUpPage(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("users/SignUp");
        return modelAndView;
    }
    @PostMapping("/sign-up")
    public String handleSignUp(@ModelAttribute("user")User user, Model model){
        if (user.getPasswordHash().trim().length() < 6){
            model.addAttribute("errSignUp", "Password at least 6 character");
            return "users/SignUp";
        }
        String passwordBeforeHashing = user.getPasswordHash();
        String newPassword = BCrypt.hashpw(passwordBeforeHashing, BCrypt.gensalt(10));
        user.setPasswordHash(newPassword);
        user.setRegisteredAt(Instant.now());
        try {
            userRepository.save(user);

        } catch (Exception e){
            model.addAttribute("errSignUp", "Email already exist!");
            user.setPasswordHash(passwordBeforeHashing);
            return "users/SignUp";
        }
        return "redirect:/";
    }
    @GetMapping("/sign-in")
    public String openSignInPage(){
        return "users/SignIn";
    }
    @PostMapping("/sign-in")
    public String handleSignIn(@RequestParam("email") String email, @RequestParam("password") String password
            , HttpServletRequest request, Model model){

        if (email.trim().isEmpty() || password.trim().isEmpty()){
            model.addAttribute("errSignIn", "Không được để trống field nào!");
            return "users/SignIn";
        }
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null){
            model.addAttribute("errSignIn", "Tài khoản không tồn tại!");
            return "users/SignIn";
        }
        boolean compare = BCrypt.checkpw(password, user.getPasswordHash());
        if (!compare){
            model.addAttribute("errSignIn", "Mật khẩu không đúng!");
            return "users/SignIn";
        }

        HttpSession session = request.getSession();
        session.setAttribute("account", user);
        return "redirect:/";
    }
    @GetMapping("/sign-out")
    public String handleSignOut(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
}
