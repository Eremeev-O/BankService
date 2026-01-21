package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.skypro.bank.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationsServiceSpring {
    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleEvaluator evaluator;

    public RecommendationsServiceSpring(List<RecommendationRuleSet> staticRules,
                                        DynamicRuleRepository dynamicRuleRepository,
                                        DynamicRuleEvaluator evaluator) {
        this.staticRules = staticRules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.evaluator = evaluator;
    }

    public Recomendations recomendations(UUID userId) {
        List<Dto> result = new ArrayList<>();


        staticRules.forEach(rule -> rule.check(userId).ifPresent(result::add));

        List<RecommendationRuleEntity> dynamicRules = dynamicRuleRepository.findAll();
        for (RecommendationRuleEntity rule : dynamicRules) {
            boolean allMatch = rule.getRuleQueries().stream()
                    .allMatch(q -> evaluator.evaluate(userId, q));

            if (allMatch) {
                result.add(new Dto(rule.getProductName(), rule.getProductId(), rule.getProductText()));
            }
        }

        return new Recomendations(userId, result);
    }
}
