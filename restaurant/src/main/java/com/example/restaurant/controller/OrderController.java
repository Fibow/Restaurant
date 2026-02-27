package com.example.restaurant.controller;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository; // Добавили репозиторий блюд

    public OrderController(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    public List<RestaurantOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public RestaurantOrder createOrder(@RequestBody RestaurantOrder order) {
        if (order.getItems() != null && !order.getItems().isEmpty()) {

            List<Long> itemIds = order.getItems().stream()
                    .map(MenuItem::getId)
                    .toList();

            List<MenuItem> fullItems = menuItemRepository.findAllById(itemIds);

            order.setItems(fullItems);
        }

        return orderRepository.save(order);
    }
}