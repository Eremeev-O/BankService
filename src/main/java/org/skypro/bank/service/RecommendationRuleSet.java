package org.skypro.bank.service;

import org.skypro.bank.model.BankProductDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<BankProductDto> check(UUID user);
}
