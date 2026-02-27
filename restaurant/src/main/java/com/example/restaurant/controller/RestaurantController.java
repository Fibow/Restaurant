package com.example.restaurant.controller;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    // Получить все рестораны
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    // Добавить новый ресторан
    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return repository.save(restaurant);
    }
}