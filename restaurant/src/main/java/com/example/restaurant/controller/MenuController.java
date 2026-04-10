package com.example.restaurant.controller;

import com.example.restaurant.repository.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    private final MenuItemRepository menuItemRepository; // Используем твой репозиторий

    public MenuController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/menu")
    public String showMenu(Model model) {
        model.addAttribute("MenuItem", menuItemRepository.findAll());
        return "/user/menu";
    }
}