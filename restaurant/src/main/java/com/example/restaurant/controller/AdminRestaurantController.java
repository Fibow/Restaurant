package com.example.restaurant.controller;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantRepository restaurantRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "admin/restaurants";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "admin/restaurant-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("restaurant") Restaurant restaurant,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "admin/restaurant-form";
        }
        restaurantRepository.save(restaurant);
        return "redirect:/admin/restaurants";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        restaurantRepository.deleteById(id);
        return "redirect:/admin/restaurants";
    }
}