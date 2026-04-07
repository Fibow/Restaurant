package com.example.restaurant.controller;

import com.example.restaurant.entity.User;
import com.example.restaurant.service.UserService;
import com.example.restaurant.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public AuthController(UserService userService, PasswordEncoder encoder, EmailService emailService) {
        this.userService = userService;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            String rawPassword = user.getPassword();
            user.setPassword(encoder.encode(rawPassword));
            user.setRole("USER");

            userService.register(user);

            String text = "Здравствуйте," + user.getUsername() + "!\n" +
                    "Ваш аккаунт в системе ресторана успешно регистрирован.\n" +
                    "Логин: " + user.getUsername() + "\n" +
                    "Email: " + user.getEmail();

            emailService.sendEmail(user.getEmail(), "Регистрация в системе", text);

            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}