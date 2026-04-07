package com.example.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        // Обязательно указываем отправителя (тот же, что в свойствах)
        message.setFrom("gebrik08@gmail.com");

        // Убираем возможные пробелы из адреса получателя
        message.setTo(to.trim());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}