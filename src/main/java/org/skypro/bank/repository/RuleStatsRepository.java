package org.skypro.bank.repository;

import org.skypro.bank.model.entity.RuleStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RuleStatsRepository extends JpaRepository<RuleStatEntity, UUID> {
}