package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("")
public class CommonController {

    @GetMapping("")
    public String openHomePage(){
        return "index";
    }
}
