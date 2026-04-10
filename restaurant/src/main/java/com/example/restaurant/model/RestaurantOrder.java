package com.example.restaurant.model;

import com.example.restaurant.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class RestaurantOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String status;
    private Long restaurantId;
    private Double totalPrice;

    @ManyToMany
    @JoinTable(
            name = "order_items", // Название промежуточной таблицы
            joinColumns = @JoinColumn(name = "order_id"), // Колонка для ID заказа
            inverseJoinColumns = @JoinColumn(name = "menu_item_id") // Колонка для ID блюда
    )
    private List<MenuItem> items;

}