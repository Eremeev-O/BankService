package org.skypro.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.bank.model.entity.RuleQueryEntity;
import org.skypro.bank.repository.RecommendationsRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamicRuleEvaluatorTest {

    @Mock
    private RecommendationsRepository repository;

    @InjectMocks
    private DynamicRuleEvaluator evaluator;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void evaluateUserOf_ShouldReturnTrue_WhenRepositoryReturnsTrue() {
        RuleQueryEntity query = new RuleQueryEntity();
        query.setQuery("USER_OF");
        query.setArguments(List.of("DEBIT"));
        query.setNegate(false);

        when(repository.isUserOf(userId, "DEBIT")).thenReturn(true);

        boolean result = evaluator.evaluate(userId, query);

        assertTrue(result);
        verify(repository).isUserOf(userId, "DEBIT");
    }

    @Test
    void evaluateWithNegate_ShouldFlipResult() {

        RuleQueryEntity query = new RuleQueryEntity();
        query.setQuery("USER_OF");
        query.setArguments(List.of("DEBIT"));
        query.setNegate(true);

        when(repository.isUserOf(userId, "DEBIT")).thenReturn(true);

        boolean result = evaluator.evaluate(userId, query);

        assertFalse(result);
    }
}