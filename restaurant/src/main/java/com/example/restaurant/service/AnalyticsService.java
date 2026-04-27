package com.example.restaurant.service;

import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public AnalyticsService(OrderRepository orderRepository,
                            MenuItemRepository menuItemRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public double getTotalRevenue() {
        Double revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalMenuItems() {
        return menuItemRepository.count();
    }

    public long getNewOrdersCount() {
        return orderRepository.countByStatus("НОВЫЙ");
    }

    public long getInProgressCount() {
        return orderRepository.countByStatus("В РАБОТЕ");
    }

    public long getReadyCount() {
        return orderRepository.countByStatus("ГОТОВ");
    }

    public long getDoneCount() {
        return orderRepository.countByStatus("ВЫДАН");
    }

    public Map<String, Long> getTopMenuItems() {
        Map<String, Long> result = new LinkedHashMap<>();
        List<Object[]> rows = orderRepository.getTopMenuItems();
        rows.stream().limit(5).forEach(row ->
                result.put((String) row[0], (Long) row[1])
        );
        return result;
    }

    public Map<String, Double> getRevenueByCategory() {
        Map<String, Double> result = new LinkedHashMap<>();
        List<Object[]> rows = orderRepository.getRevenueByCategory();
        rows.forEach(row -> {
            String category = row[0] != null ? (String) row[0] : "Без категории";
            Double revenue = ((Number) row[1]).doubleValue();
            result.put(category, revenue);
        });
        return result;
    }
}