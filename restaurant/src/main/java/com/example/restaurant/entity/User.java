package com.example.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, message = "Имя пользователя должно быть не менее 3 символов")
    private String username;
    @NotBlank
    @Email(message = "Введите корректный email")
    @Column(unique = true)
    private String email;
    @NotNull
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
    private String role;
}