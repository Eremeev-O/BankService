package org.skypro.bank.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Сущность для хранения статистики срабатывания правил.
 */
@Entity
@Table(name = "rule_stats")
public class RuleStatEntity {
    @Id
    private UUID ruleId;
    private long count;

    public RuleStatEntity() {
    }

    public RuleStatEntity(UUID ruleId) {
        this.ruleId = ruleId;
        this.count = 0;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public long getCount() {
        return count;
    }

    public void increment() {
        this.count++;
    }
}