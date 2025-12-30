package com.techmatrix18.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

/**
 * Simple Telegram bot example that sends a message.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 25-12-2025
 * @version 0.0.1
 */
public class MyTelegramBot {
    private final TelegramBot bot;

    public MyTelegramBot() {
        this.bot = new TelegramBot("8588196509:AAF2phFKo4tf8ZJziZtsgSo3d8qzJD6S8WU");
    }

    public void start() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                // Проверяем, есть ли в обновлении текстовое сообщение
                if (update.message() != null && update.message().text() != null) {
                    long chatId = update.message().chat().id();
                    String messageText = update.message().text();

                    System.out.println("Получено сообщение: " + messageText + " от чата ID: " + chatId);

                    // 3. Отправка ответа пользователю
                    bot.execute(new SendMessage(chatId, "Вы сказали: " + messageText));
                }
            }
            // Подтверждаем получение всех обновлений
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}

