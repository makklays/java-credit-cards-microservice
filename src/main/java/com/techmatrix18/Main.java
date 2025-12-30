package com.techmatrix18;

import com.techmatrix18.bot.MyTelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class to run the Spring Boot application.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Init and start the Telegram bot
        MyTelegramBot telegramBot = new MyTelegramBot();
        telegramBot.start();
        System.out.println("Бот успешно инициализирован и слушает сообщения.");

        SpringApplication.run(Main.class, args);
    }
}

