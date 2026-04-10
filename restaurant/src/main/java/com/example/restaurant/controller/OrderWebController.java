package com.example.restaurant.controller;

import com.example.restaurant.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;

    public OrderWebController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam("itemId") Long itemId, Principal principal) {
        orderService.createSimpleOrder(itemId, principal.getName());
        return "redirect:/orders/my";
    }

    @GetMapping("/my")
    public String myOrders(Model model, Principal principal) {
        model.addAttribute("orders", orderService.getUserOrders(principal.getName()));
        return "user/orders";
    }
}