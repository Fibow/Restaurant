package com.example.restaurant.controller;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.RestaurantOrder;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "Управление заказами ресторана и расчет стоимости")
public class OrderController {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository; // Добавили репозиторий блюд

    public OrderController(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    @Operation(summary = "Получить список всех заказов")
    public List<RestaurantOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<RestaurantOrder> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить заказ целиком", description = "Полностью заменяет данные заказа (статус, ресторан, список блюд) и пересчитывает сумму")
    public ResponseEntity<RestaurantOrder> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantOrder updatedOrder) {

        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(updatedOrder.getStatus());
                    existingOrder.setRestaurantId(updatedOrder.getRestaurantId());

                    if (updatedOrder.getItems() != null && !updatedOrder.getItems().isEmpty()) {

                        List<Long> itemIds = updatedOrder.getItems().stream()
                                .map(MenuItem::getId)
                                .toList();

                        List<MenuItem> fullItems = menuItemRepository.findAllById(itemIds);

                        Double calculatedTotal = fullItems.stream()
                                .mapToDouble(item -> item.getPrice() != null ? item.getPrice() : 0.0)
                                .sum();

                        existingOrder.setItems(fullItems);
                        existingOrder.setTotalPrice(calculatedTotal);
                    } else {
                        existingOrder.setItems(null);
                        existingOrder.setTotalPrice(0.0);
                    }

                    return ResponseEntity.ok(orderRepository.save(existingOrder));
                })
                .orElse(ResponseEntity.notFound().build()); // Если заказ с таким ID не найден, вернем ошибку 404
    }

    @PostMapping
    @Operation(
            summary = "Оформить новый заказ",
            description = "Принимает список ID блюд и автоматически рассчитывает итоговую сумму заказа"
    )
    public RestaurantOrder createOrder(@RequestBody RestaurantOrder order) {

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            List<Long> itemIds = order.getItems().stream()
                    .map(MenuItem::getId)
                    .toList();

            List<MenuItem> fullItems = menuItemRepository.findAllById(itemIds);

            Double calculatedTotal = fullItems.stream()
                    .mapToDouble(item -> item.getPrice() != null ? item.getPrice() : 0.0)
                    .sum();

            order.setItems(fullItems);
            order.setTotalPrice(calculatedTotal);
        } else {
            order.setTotalPrice(0.0);
        }

        return orderRepository.save(order);
    }
}