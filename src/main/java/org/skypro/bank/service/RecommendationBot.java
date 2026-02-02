package org.skypro.bank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.repository.RecommendationsRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
                // ТЗ: приветствует и печатает справку
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

        // ТЗ: Если не найден ИЛИ найдено несколько -> "Пользователь не найден"
        if (users.size() != 1) {
            sendText(chatId, "Пользователь не найден");
            return;
        }

        Map<String, Object> user = users.get(0);
        UUID userId = UUID.fromString(user.get("ID").toString());
        String firstName = (String) user.get("FIRST_NAME");
        String lastName = (String) user.get("LAST_NAME");

        Recomendations recs = recommendationsService.recomendations(userId);

        // ТЗ: Здравствуйте <Имя и фамилия пользователя>
        StringBuilder response = new StringBuilder("Здравствуйте " + firstName + " " + lastName + "\n");
        response.append("Новые продукты для вас:\n");

        if (recs.getRecomendations().isEmpty()) {
            response.append("На данный момент предложений нет.");
        } else {
            recs.getRecomendations().forEach(dto ->
                    // ТЗ: удобно отформатированный список
                    response.append("● ").append(dto.getName()).append("\n")
                            .append(dto.getText()).append("\n\n")
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