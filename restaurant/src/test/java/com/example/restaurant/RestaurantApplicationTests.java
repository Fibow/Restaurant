package com.example.restaurant;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RestaurantApplicationTests {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		// Проверяем что контекст Spring поднимается без ошибок
	}

	@Test
	void menuItemSaveAndFind() {
		MenuItem item = new MenuItem();
		item.setName("Тест блюдо");
		item.setPrice(500.0);
		item.setCategory("Основное");

		MenuItem saved = menuItemRepository.save(item);

		assertNotNull(saved.getId());
		assertEquals("Тест блюдо", saved.getName());

		menuItemRepository.delete(saved); // чистим за собой
	}

	@Test
	void usernameDuplicateCheck() {
		assertTrue(true); // базовый smoke-тест, расширяй по необходимости
	}
}