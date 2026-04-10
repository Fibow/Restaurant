package com.example.restaurant.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/navbar")
    public String navbar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return "/fragments/navbar";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "profile";
    }
}