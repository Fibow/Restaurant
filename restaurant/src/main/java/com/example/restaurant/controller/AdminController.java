package com.example.restaurant.controller;

import com.example.restaurant.entity.User;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.EmailService;
import com.example.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;

    public AdminController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create-user";
    }

    @PostMapping("/create")
    public String createUser(@Valid User user) {
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);
        userService.register(user);

        String text = "Здравствуйте!\n\n" +
                "Ваш аккаунт в системе ресторана успешно создан.\n" +
                "Логин: " + user.getUsername() + "\n" +
                "Пароль: " + rawPassword;

        String recipient = user.getUsername().trim();

        if (recipient.contains("@")) {
            emailService.sendEmail(recipient, "Успешная регистрация", text);
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/edit")
    public String updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setRole(user.getRole());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {

                String rawNewPassword = user.getPassword();

                existingUser.setPassword(passwordEncoder.encode(rawNewPassword));

                String text = "Здравствуйте!\n\n" +
                        "Ваш пароль в системе ресторана был изменен администратором.\n" +
                        "Логин: " + existingUser.getUsername() + "\n" +
                        "Новый пароль: " + rawNewPassword;

                String recipient = existingUser.getUsername().trim();
                if (recipient.contains("@")) {
                    emailService.sendEmail(recipient, "Изменение пароля", text);
                }
            }

            userRepository.save(existingUser);
        }
        return "redirect:/admin/users";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
