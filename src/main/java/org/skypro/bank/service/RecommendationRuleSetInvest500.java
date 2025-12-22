package org.skypro.bank.service;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface RecommendationRuleSetInvest500 {
    Optional recomendationsInvest500(UUID user);
}
