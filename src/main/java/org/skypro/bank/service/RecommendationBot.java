package org.skypro.bank.service;

import org.skypro.bank.model.Recomendations;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Telegram-бот для предоставления банковских рекомендаций через мессенджер.
 * Поддерживает команды /start и /recommend {username}.
 */
@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private final RecommendationsRepository recommendationsRepository;
    private final RecommendationsServiceSpring recommendationsService;

    public RecommendationBot(@Value("${telegram.bot.token}") String botToken,
                             RecommendationsRepository recommendationsRepository,
                             RecommendationsServiceSpring recommendationsService) {
        super(botToken);
        this.recommendationsRepository = recommendationsRepository;
        this.recommendationsService = recommendationsService;
    }

    @Override
    public String getBotUsername() {
        return "blabla63_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (text.startsWith("/start")) {
                sendText(chatId, "Привет! Я бот банковских рекомендаций.\n" +
                        "Чтобы получить предложения, используйте команду:\n" +
                        "/recommend username");
            } else if (text.startsWith("/recommend")) {
                handleRecommend(chatId, text);
            }
        }
    }

    private void handleRecommend(long chatId, String command) {
        String[] parts = command.split("\\s+");
        if (parts.length < 2) {
            sendText(chatId, "Пользователь не найден"); // Или инструкцию
            return;
        }

        String username = parts[1];
        List<Map<String, Object>> users = recommendationsRepository.findUserByName(username);

        if (users.size() != 1) {
            sendText(chatId, "Пользователь не найден");
            return;
        }

        Map<String, Object> user = users.get(0);
        UUID userId = UUID.fromString(user.get("ID").toString());
        String firstName = (String) user.get("FIRST_NAME");
        String lastName = (String) user.get("LAST_NAME");

        Recomendations recs = recommendationsService.recomendations(userId);

        StringBuilder response = new StringBuilder("Здравствуйте " + firstName + " " + lastName + "\n");
        response.append("Новые продукты для вас:\n");

        if (recs.recomendations().isEmpty()) {
            response.append("На данный момент предложений нет.");
        } else {
            recs.recomendations().forEach(dto ->
                    response.append("● ").append(dto.name()).append("\n")
                            .append(dto.text()).append("\n\n")
            );
        }

        sendText(chatId, response.toString());
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}