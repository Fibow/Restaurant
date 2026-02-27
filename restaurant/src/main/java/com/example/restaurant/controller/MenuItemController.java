package com.example.restaurant.controller;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.repository.MenuItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Меню", description = "Управление позициями в меню ресторана")
public class MenuItemController {

    private final MenuItemRepository repository;

    public MenuItemController(MenuItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Получить список всех блюд")
    public List<MenuItem> getAllMenuItems() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить блюдо по ID")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить блюдо из меню")
    public void deleteMenuItem(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping
    @Operation(summary = "Добавить новое блюдо в меню")
    public MenuItem createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        return repository.save(menuItem);
    }
}