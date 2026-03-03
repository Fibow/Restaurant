package com.example.restaurant.controller;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Рестораны", description = "Управление информацией о заведениях")
public class RestaurantController {

    private final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Получить список всех ресторанов")
    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Добавить новый ресторан")
    public Restaurant createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @PostMapping("/by-params")
    @Operation(summary = "Создать ресторан с помощью params")
    public Restaurant createRestaurantWithParams(
            @RequestParam String name,
            @RequestParam String address) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);

        return repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан по ID")
    public void deleteRestaurant(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные ресторана", description = "Изменяет название или адрес существующего ресторана по его ID")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,@Valid @RequestBody Restaurant updatedRestaurant) {
        return repository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(updatedRestaurant.getName());
                    restaurant.setAddress(updatedRestaurant.getAddress());
                    Restaurant savedRestaurant = repository.save(restaurant);
                    return ResponseEntity.ok(savedRestaurant);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}