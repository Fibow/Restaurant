package com.example.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemDto {
    private Long id;

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть > 0")
    private Double price;

    @NotBlank(message = "Категория обязательна")
    private String category;
}