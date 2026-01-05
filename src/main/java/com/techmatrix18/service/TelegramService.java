package com.techmatrix18.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with Telegram Bot API.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Service
public class TelegramService {
    private final TelegramBot bot;

    public TelegramService() {
        this.bot = new TelegramBot("8588196509:AAF2phFKo4tf8ZJziZtsgSo3d8qzJD6S8WU");
    }

    public TelegramBot getBot() {
        return bot;
    }

    public void sendMessage(Long chatId, String text) {
        SendResponse response = bot.execute(new SendMessage(chatId, text));
        if (!response.isOk()) {
            System.err.println("Failed to send message to chatId " + chatId + ": " + response.description());
        }
    }
}

