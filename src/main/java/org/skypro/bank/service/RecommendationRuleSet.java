package org.skypro.bank.service;

import org.skypro.bank.model.Dto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<Dto> check (UUID user);
}
