package com.example.restaurant.service;

import com.example.restaurant.dto.RegistrationDto;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }
    @Transactional
    public void register(RegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Пользователь с таким логином уже существует");
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(User updated, String rawNewPassword) {
        User existing = userRepository.findById(updated.getId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        existing.setUsername(updated.getUsername());
        existing.setRole(updated.getRole());

        if (rawNewPassword != null && !rawNewPassword.isEmpty()) {
            existing.setPassword(passwordEncoder.encode(rawNewPassword));
        }

        userRepository.save(existing);
    }
}