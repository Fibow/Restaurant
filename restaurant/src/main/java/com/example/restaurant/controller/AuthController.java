package com.example.restaurant.controller;

import com.example.restaurant.dto.RegistrationDto;
import com.example.restaurant.service.EmailService;
import com.example.restaurant.service.UserService;
import com.example.restaurant.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          VerificationService verificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegistrationDto dto,
                           BindingResult result,
                           Model model,
                           HttpSession session) {
        if (result.hasErrors()) {
            return "register";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Пароли не совпадают!");
            return "register";
        }

        try {
            // Проверяем уникальность ДО отправки кода
            userService.checkUnique(dto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }

        // Сохраняем данные в сессии и отправляем код
        session.setAttribute("pendingRegistration", dto);

        String code = verificationService.generateCode(dto.getEmail());
        emailService.sendEmail(
                dto.getEmail(),
                "Подтверждение регистрации",
                "Ваш код подтверждения: " + code + "\n\nВведите его на странице подтверждения."
        );

        return "redirect:/verify-email";
    }

    @GetMapping("/verify-email")
    public String verifyPage(HttpSession session, Model model) {
        RegistrationDto dto = (RegistrationDto) session.getAttribute("pendingRegistration");
        if (dto == null) {
            return "redirect:/register";
        }
        // Показываем только часть email для конфиденциальности
        model.addAttribute("email", maskEmail(dto.getEmail()));
        return "verify-email";
    }

    @PostMapping("/verify-email")
    public String verify(@RequestParam String code,
                         HttpSession session,
                         Model model) {
        RegistrationDto dto = (RegistrationDto) session.getAttribute("pendingRegistration");

        if (dto == null) {
            return "redirect:/register";
        }

        if (!verificationService.verify(dto.getEmail(), code)) {
            model.addAttribute("email", maskEmail(dto.getEmail()));
            model.addAttribute("errorMessage", "Неверный код. Попробуйте ещё раз.");
            return "verify-email";
        }

        // Код верный — создаём пользователя
        try {
            userService.register(dto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }

        verificationService.remove(dto.getEmail());
        session.removeAttribute("pendingRegistration");

        return "redirect:/login?registered";
    }

    @PostMapping("/resend-code")
    public String resendCode(HttpSession session, Model model) {
        RegistrationDto dto = (RegistrationDto) session.getAttribute("pendingRegistration");

        if (dto == null) {
            return "redirect:/register";
        }

        String code = verificationService.generateCode(dto.getEmail());
        emailService.sendEmail(
                dto.getEmail(),
                "Новый код подтверждения",
                "Ваш новый код подтверждения: " + code
        );

        model.addAttribute("email", maskEmail(dto.getEmail()));
        model.addAttribute("successMessage", "Новый код отправлен.");
        return "verify-email";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Маскируем email: gebrik08@gmail.com → ge****@gmail.com
    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 2) return email;
        return email.substring(0, 2) + "****" + email.substring(at);
    }
}