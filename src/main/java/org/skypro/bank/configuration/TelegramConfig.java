package org.skypro.bank.configuration;

import org.skypro.bank.service.RecommendationBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Конфигурация для регистрации Telegram-бота в системе.
 */
@Configuration
public class TelegramConfig {

    /**
     * Инициализирует API Telegram-ботов и регистрирует бота.
     *
     * @param recommendationBot экземпляр бота
     * @return настроенный TelegramBotsApi
     * @throws TelegramApiException при ошибке регистрации бота
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(RecommendationBot recommendationBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(recommendationBot);
        return botsApi;
    }
}