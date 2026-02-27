package com.example.restaurant.repository;

import com.example.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Restaurant, Long> {}