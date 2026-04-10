package com.example.restaurant.controller;

import com.example.restaurant.entity.User;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.OrderService;
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
    private OrderService orderService;
    @PostMapping("/add")
    public String addOrder(@RequestParam("itemId") Long itemId, Principal principal) {
        orderService.createSimpleOrder(itemId, principal.getName());
        return "redirect:/orders/my";
    }
}