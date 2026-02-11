package org.skypro.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.bank.model.BankProductDto;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.skypro.bank.model.entity.RuleQueryEntity;
import org.skypro.bank.repository.DynamicRuleRepository;
import org.skypro.bank.repository.RuleStatsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsServiceSpringTest {

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;
    @Mock
    private DynamicRuleEvaluator evaluator;
    @Mock
    private RuleStatsRepository statsRepository;

    private final List<RecommendationRuleSet> staticRules = new ArrayList<>();

    @InjectMocks
    private RecommendationsServiceSpring service;

    @Test
    void recomendations_ShouldReturnCombinedResults() {
        UUID userId = UUID.randomUUID();

        // 1. Настройка статического правила
        RecommendationRuleSet staticRule = mock(RecommendationRuleSet.class);
        staticRules.add(staticRule);
        service = new RecommendationsServiceSpring(staticRules, dynamicRuleRepository, evaluator, statsRepository);

        BankProductDto staticProduct = new BankProductDto("Static", UUID.randomUUID(), "Text");
        when(staticRule.check(userId)).thenReturn(Optional.of(staticProduct));

        // 2. Настройка динамического правила
        RecommendationRuleEntity dynamicRule = new RecommendationRuleEntity();
        dynamicRule.setId(UUID.randomUUID());
        dynamicRule.setProductName("Dynamic");
        dynamicRule.setProductId(UUID.randomUUID());

        RuleQueryEntity query = new RuleQueryEntity();
        dynamicRule.setRuleQueries(List.of(query));

        when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));
        when(evaluator.evaluate(userId, query)).thenReturn(true);

        Recomendations result = service.recomendations(userId);

        assertEquals(2, result.recomendations().size());
        verify(statsRepository).incrementCount(dynamicRule.getId()); // Проверяем, что статистика обновилась
    }
}