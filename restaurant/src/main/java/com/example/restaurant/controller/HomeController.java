package com.example.restaurant.controller;

import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        // Берем имя пользователя из сессии и кладем его в переменную "username"
        model.addAttribute("username", auth.getName());
        return "profile";
    }
}