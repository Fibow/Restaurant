package com.example.restaurant.controller;

import com.example.restaurant.dto.DishDto;
import com.example.restaurant.service.DishService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dishes")
public class AdminDishController {

    private final DishService dishService;

    public AdminDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/add")
    public String showAddDishForm(Model model) {
        model.addAttribute("dishDto", new DishDto()); // Пустой объект для формы
        return "admin/add_dish";
    }

    @PostMapping("/add")
    public String addDish(@Valid @ModelAttribute("dishDto") DishDto dishDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/add_dish";
        }
        dishService.saveDish(dishDto);
        return "redirect:/menu"; // Или на страницу со списком блюд
    }
}