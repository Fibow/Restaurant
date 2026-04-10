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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final EmailService emailService;

    // ✅ Только конструктор, никаких @Autowired полей
    public AdminController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers()); // ✅
        return "admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // ✅
        return "redirect:/admin/users";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create-user";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/create-user";
        }

        try {
            userService.register(user);

            emailService.sendEmail(
                    user.getEmail(),
                    "Аккаунт создан",
                    "Здравствуйте, " + user.getUsername() + "!\n\n" +
                            "Ваш аккаунт в системе ресторана создан.\n" +
                            "Для входа используйте ваш логин: " + user.getUsername()
            );

            return "redirect:/admin/users";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/create-user";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id).orElse(null));
        return "admin/edit-user";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/edit-user";
        }
        String rawPassword = user.getPassword();
        userService.updateUser(user, rawPassword);

        if (rawPassword != null && !rawPassword.isEmpty()) {
            emailService.sendEmail(
                    user.getEmail(),
                    "Изменение пароля",
                    "Ваш пароль был изменён администратором."
            );
        }
        return "redirect:/admin/users";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
