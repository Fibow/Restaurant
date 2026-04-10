package com.example.restaurant.controller;

import com.example.restaurant.dto.MenuItemDto;
import com.example.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/dishes")
public class AdminMIController {

    private final MenuItemService menuItemService;

    public AdminMIController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/add")
    public String showAddDishForm(Model model) {
        model.addAttribute("MenuItemDto", new MenuItemDto());
        return "admin/add_dish";
    }

    @PostMapping("/add")
    public String addDish(@Valid @ModelAttribute("MenuItemDto") MenuItemDto menuItemDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            return "admin/add_dish";
        }
        menuItemService.saveMenuItem(menuItemDto);
        return "redirect:/menu"; // Или на страницу со списком блюд
    }

    @PostMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menu";
    }
}