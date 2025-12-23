package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetSimpleloan implements RecommendationRuleSet {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationRuleSetSimpleloan(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<Dto> check(UUID user) {
        Boolean resultA = !recommendationsRepository.getProductCheck(user, "CREDIT");
        Boolean resultB = recommendationsRepository.getProductSum(user, "DEBIT", "DEPOSIT") > recommendationsRepository.getProductSum(user, "DEBIT", "WITHDRAW");
        Boolean resultC = recommendationsRepository.getProductSum(user, "DEBIT", "DEPOSIT") > 100000;

        if (resultA && resultB && resultC) {
            UUID id = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
            String recom = "Простой кредит";
            String text = "Откройте мир выгодных кредитов с нами!\n" +
                    "\n" +
                    "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n" +
                    "\n" +
                    "Почему выбирают нас:\n" +
                    "\n" +
                    "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n" +
                    "\n" +
                    "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n" +
                    "\n" +
                    "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n" +
                    "\n" +
                    "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";
            Optional<Dto> recomendation = Optional.of(new Dto(recom, id, text));
            return recomendation;
        }
        return Optional.empty();
    }
}
