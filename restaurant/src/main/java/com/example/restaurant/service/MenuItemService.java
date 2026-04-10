package com.example.restaurant.service;

import com.example.restaurant.dto.MenuItemDto;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
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

    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> getAllItems() {
        return menuItemRepository.findAll();
    }
}