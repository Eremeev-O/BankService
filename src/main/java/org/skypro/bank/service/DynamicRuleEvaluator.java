package org.skypro.bank.service;

import org.skypro.bank.model.entity.RuleQueryEntity;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DynamicRuleEvaluator {
    private final RecommendationsRepository repository;

    public DynamicRuleEvaluator(RecommendationsRepository repository) {
        this.repository = repository;
    }

    public boolean evaluate(UUID userId, RuleQueryEntity queryEntity) {
        boolean result = switch (queryEntity.getQuery()) {
            case "USER_OF" ->
                    repository.isUserOf(userId, queryEntity.getArguments().get(0));
            case "ACTIVE_USER_OF" ->
                    repository.isActiveUserOfProduct(userId, queryEntity.getArguments().get(0));
            case "TRANSACTION_SUM_COMPARE" ->
                    repository.isTransactionsSumCompare(userId,
                            queryEntity.getArguments().get(0),
                            queryEntity.getArguments().get(1),
                            queryEntity.getArguments().get(2),
                            Integer.parseInt(queryEntity.getArguments().get(3)));
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" ->
                    repository.isTransactionSumCompareDepositWithdraw(userId,
                            queryEntity.getArguments().get(0),
                            queryEntity.getArguments().get(1));
            default -> false;
        };

        return queryEntity.isNegate() ? !result : result;
    }
}