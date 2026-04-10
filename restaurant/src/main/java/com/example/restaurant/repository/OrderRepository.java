package com.example.restaurant.repository;

import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.User;
import com.example.restaurant.model.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<RestaurantOrder, Long> {
    List<Order> findByUserId(Long userId);

    List<RestaurantOrder> findAllByUser(User user);
}