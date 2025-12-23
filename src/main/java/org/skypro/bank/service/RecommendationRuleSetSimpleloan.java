package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationRuleSetSimpleloan implements RecommendationRuleSet{
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationRuleSetSimpleloan(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<Dto> check(UUID user) {
        return Optional.empty();
    }
}
