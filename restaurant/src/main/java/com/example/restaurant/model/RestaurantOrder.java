package com.example.restaurant.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class RestaurantOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private Long restaurantId;

    @ManyToMany
    @JoinTable(
            name = "order_items", // Название промежуточной таблицы
            joinColumns = @JoinColumn(name = "order_id"), // Колонка для ID заказа
            inverseJoinColumns = @JoinColumn(name = "menu_item_id") // Колонка для ID блюда
    )
    private List<MenuItem> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}