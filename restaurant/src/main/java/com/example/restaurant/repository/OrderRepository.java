package com.example.restaurant.repository;

import com.example.restaurant.entity.User;
import com.example.restaurant.model.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<RestaurantOrder, Long> {

    List<RestaurantOrder> findAllByUser(User user);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM RestaurantOrder o")
    Double getTotalRevenue();

    long countByStatus(String status);

    @Query("""
        SELECT i.name, COUNT(i.id)
        FROM RestaurantOrder o
        JOIN o.items i
        GROUP BY i.name
        ORDER BY COUNT(i.id) DESC
        """)
    List<Object[]> getTopMenuItems();

    @Query("""
        SELECT i.category, COALESCE(SUM(i.price), 0)
        FROM RestaurantOrder o
        JOIN o.items i
        GROUP BY i.category
        ORDER BY SUM(i.price) DESC
        """)
    List<Object[]> getRevenueByCategory();
}