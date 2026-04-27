package com.example.restaurant.service;

import com.example.restaurant.entity.User;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        MenuItemRepository menuItemRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createSimpleOrder(Long menuItemId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        RestaurantOrder order = new RestaurantOrder();
        order.setUser(user);
        order.setStatus("НОВЫЙ");
        order.setTotalPrice(item.getPrice());
        order.setCreatedAt(LocalDateTime.now()); // ← добавь эту строку

        RestaurantOrder savedOrder = orderRepository.save(order);

        savedOrder.getItems().add(item);
        orderRepository.save(savedOrder);
    }

    @Transactional
    public void updateStatus(Long orderId, String status) {
        RestaurantOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public List<RestaurantOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<RestaurantOrder> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return orderRepository.findAllByUser(user);
    }
}
