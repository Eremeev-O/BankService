package org.skypro.bank.service;

import org.skypro.bank.model.BankProductDto;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.skypro.bank.model.entity.RuleStatEntity;
import org.skypro.bank.repository.DynamicRuleRepository;
import org.skypro.bank.repository.RuleStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для формирования персональных рекомендаций пользователям.
 * Комбинирует заданные правила и динамические правила из базы данных.
 */
@Service
public class RecommendationsServiceSpring {
    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleEvaluator evaluator;
    private final RuleStatsRepository statsRepository;

    public RecommendationsServiceSpring(List<RecommendationRuleSet> staticRules,
                                        DynamicRuleRepository dynamicRuleRepository,
                                        DynamicRuleEvaluator evaluator,
                                        RuleStatsRepository statsRepository) {
        this.staticRules = staticRules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.evaluator = evaluator;
        this.statsRepository = statsRepository;
    }

    /**
     * Формирует список рекомендаций для указанного пользователя.
     *
     * @param userId уникальный идентификатор пользователя
     * @return объект Recomendations, содержащий список подходящих продуктов
     */
    @Transactional
    public Recomendations recomendations(UUID userId) {
        List<BankProductDto> result = new ArrayList<>();
        staticRules.forEach(rule -> rule.check(userId).ifPresent(result::add));

        List<RecommendationRuleEntity> dynamicRules = dynamicRuleRepository.findAll();
        for (RecommendationRuleEntity rule : dynamicRules) {
            boolean allMatch = rule.getRuleQueries().stream()
                    .allMatch(q -> evaluator.evaluate(userId, q));

            if (allMatch) {
                result.add(new BankProductDto(rule.getProductName(), rule.getProductId(), rule.getProductText()));
                // Вызываем атомарный инкремент
                statsRepository.incrementCount(rule.getId());
            }
        }
        return new Recomendations(userId, result);
    }

    private void incrementStat(UUID ruleId) {
        RuleStatEntity stat = statsRepository.findById(ruleId)
                .orElse(new RuleStatEntity(ruleId));
        stat.increment();
        statsRepository.save(stat);
    }
}
