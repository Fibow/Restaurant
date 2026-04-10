package com.example.restaurant.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishDto {
    @NotBlank(message = "Название блюда обязательно")
    private String name;

    private String description;

    @NotNull(message = "Цена должна быть указана")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля")
    private Double price;

    @NotBlank(message = "Укажите категорию (напр. Супы, Десерты)")
    private String category;

}