package com.example.restaurant.controller;

import com.example.restaurant.entity.User;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public OrderWebController(OrderRepository orderRepository, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    // Показать историю заказов пользователя
    @GetMapping("/my")
    public String showMyOrders(Model model, Principal principal) {
        // Находим пользователя по логину из сессии
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // В репозитории заказов нужно будет добавить метод findAllByUser(user)
        model.addAttribute("orders", orderRepository.findAllByUser(user));
        return "user/orders";
    }

    // Обработка простого заказа из меню (нажал кнопку -> создался заказ с 1 блюдом)
    @PostMapping("/add-simple")
    public String addSimpleOrder(@RequestParam("dishId") Long dishId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        MenuItem item = menuItemRepository.findById(dishId).get();

        RestaurantOrder order = new RestaurantOrder();
        order.setUser(user);
        order.setItems(List.of(item)); // Кладем одно блюдо в список
        order.setTotalPrice(item.getPrice());
        order.setStatus("ПРИНЯТ");

        orderRepository.save(order);
        return "redirect:/orders/my";
    }
}