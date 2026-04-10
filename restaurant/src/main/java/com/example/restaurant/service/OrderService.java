package com.example.restaurant.service;

import com.example.restaurant.entity.User;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 1. Ищем пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // 2. Ищем блюдо
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        // 3. Создаем заказ
        RestaurantOrder order = new RestaurantOrder();
        order.setUser(user);
        order.setItems(List.of(item)); // Кладем выбранное блюдо в список
        order.setTotalPrice(item.getPrice()); // Устанавливаем цену
        order.setStatus("НОВЫЙ");

        // 4. Сохраняем
        orderRepository.save(order);
    }

    public List<RestaurantOrder> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return orderRepository.findAllByUser(user);
    }
}
