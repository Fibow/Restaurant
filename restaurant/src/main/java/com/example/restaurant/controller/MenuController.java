package com.example.restaurant.controller;

import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    private final MenuItemService menuItemService; // ✅ сервис, не репозиторий

    public MenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/menu")
    public String showMenu(Model model) {
        model.addAttribute("MenuItem", menuItemService.getAllItems()); // ✅
        return "/user/menu";
    }
}