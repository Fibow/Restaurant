package com.example.restaurant.service;

import com.example.restaurant.dto.MenuItemDto;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           OrderRepository orderRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void saveMenuItem(MenuItemDto dto) {
        MenuItem item = new MenuItem();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory());
        menuItemRepository.save(item);
    }

    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
    }

    @Transactional
    public void updateMenuItem(Long id, MenuItemDto dto) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory());
        menuItemRepository.save(item);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));

        // Убираем блюдо из всех заказов перед удалением
        List<RestaurantOrder> orders = orderRepository.findAll();
        for (RestaurantOrder order : orders) {
            order.getItems().remove(item);
        }
        orderRepository.saveAll(orders);

        menuItemRepository.delete(item);
    }

    public List<MenuItem> getAllItems() {
        return menuItemRepository.findAll();
    }
}