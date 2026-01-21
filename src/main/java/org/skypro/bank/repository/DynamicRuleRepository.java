package org.skypro.bank.repository;

import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<RecommendationRuleEntity, UUID> {
    void deleteByProductId(UUID productId);
}