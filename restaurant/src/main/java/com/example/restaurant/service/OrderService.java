package com.example.restaurant.service;

import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.DishRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, DishRepository dishRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    public void createOrder(Long dishId, String username) {
        // 1. Находим пользователя и блюдо в базе
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        // 2. Создаем заказ
        Order order = new Order();
        order.setUser(user);
        order.setDish(dish);
        order.setStatus("ОЖИДАЕТ ПОДТВЕРЖДЕНИЯ");
        order.setOrderTime(LocalDateTime.now());

        // 3. Сохраняем
        ///orderRepository.save(order);
    }

    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return orderRepository.findByUserId(user.getId());
    }
}
