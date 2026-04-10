package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь: Много заказов могут принадлежать одному пользователю
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Связь: В одном заказе (пока что) одно блюдо
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    private String status;

    private LocalDateTime orderTime;

}