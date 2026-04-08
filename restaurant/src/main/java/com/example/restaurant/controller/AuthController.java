package com.example.restaurant.controller;

import com.example.restaurant.dto.RegistrationDto;
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
    private final EmailService emailService;

    public AuthController(UserService userService, PasswordEncoder encoder, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegistrationDto registrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Пароли не совпадают!");
            return "register";
        }

        try {
            userService.register(registrationDto);
            emailService.sendEmail(registrationDto.getEmail(), "Регистрация", "Вы успешно зарегистрированы!");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}