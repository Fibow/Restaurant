package com.example.restaurant.controller;

import com.example.restaurant.entity.User;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final OrderService orderService;

    public HomeController(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

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
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getUserOrders(username));
        model.addAttribute("totalOrders", orderService.getUserOrders(username).size());
        model.addAttribute("totalSpent",
                orderService.getUserOrders(username).stream()
                        .mapToDouble(o -> o.getTotalPrice() != null ? o.getTotalPrice() : 0)
                        .sum());
        return "profile";
    }
}