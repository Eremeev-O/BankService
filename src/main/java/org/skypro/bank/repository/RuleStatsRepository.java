package org.skypro.bank.repository;

import org.skypro.bank.model.entity.RuleStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface RuleStatsRepository extends JpaRepository<RuleStatEntity, UUID> {

    @Modifying
    @Query(value = """
            INSERT INTO rule_stats (rule_id, count) 
            VALUES (:ruleId, 1) 
            ON CONFLICT (rule_id) 
            DO UPDATE SET count = rule_stats.count + 1
            """, nativeQuery = true)
    void incrementCount(@Param("ruleId") UUID ruleId);
}