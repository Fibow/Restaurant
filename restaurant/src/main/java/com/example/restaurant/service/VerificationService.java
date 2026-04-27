package com.example.restaurant.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class VerificationService {

    // email -> код
    private final Map<String, String> codes = new ConcurrentHashMap<>();

    private final Random random = new Random();

    // Генерируем 6-значный код и сохраняем
    public String generateCode(String email) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        codes.put(email, code);
        return code;
    }

    // Проверяем код
    public boolean verify(String email, String inputCode) {
        String saved = codes.get(email);
        return saved != null && saved.equals(inputCode.trim());
    }

    // Удаляем код после успешной верификации
    public void remove(String email) {
        codes.remove(email);
    }
}