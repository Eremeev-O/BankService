package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface RecommendationRuleSet {
    Optional<Dto> check (UUID user);
}
