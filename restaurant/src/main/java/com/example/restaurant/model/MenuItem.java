package com.example.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;

    private String description;

    @NotNull(message = "Цена должна быть указана")
    @Positive(message = "Цена должна быть больше нуля")
    private Double price;

    private String category;
}