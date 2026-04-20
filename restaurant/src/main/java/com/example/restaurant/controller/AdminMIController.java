package com.example.restaurant.controller;

import com.example.restaurant.dto.MenuItemDto;
import com.example.restaurant.model.MenuItem;
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MenuItem item = menuItemService.getById(id);
        MenuItemDto dto = new MenuItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setCategory(item.getCategory());
        model.addAttribute("MenuItemDto", dto);
        return "admin/edit_dish";
    }

    @PostMapping("/add")
    public String addDish(@Valid @ModelAttribute("MenuItemDto") MenuItemDto menuItemDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            return "admin/add_dish";
        }
        menuItemService.saveMenuItem(menuItemDto);
        return "redirect:/menu"; // Или на страницу со списком блюд
    }

    @PostMapping("/edit/{id}")
    public String editDish(@PathVariable Long id,
                           @Valid @ModelAttribute("MenuItemDto") MenuItemDto dto,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "admin/edit_dish";
        }
        menuItemService.updateMenuItem(id, dto);
        return "redirect:/menu";
    }

    @PostMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menu";
    }
}