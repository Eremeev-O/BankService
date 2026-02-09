package org.skypro.bank.repository;

import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с динамическими правилами в основной БД.
 */
@Repository
public interface DynamicRuleRepository extends JpaRepository<RecommendationRuleEntity, UUID> {

    @EntityGraph(attributePaths = {"ruleQueries"})
    List<RecommendationRuleEntity> findAll();

    void deleteByProductId(UUID productId);
}