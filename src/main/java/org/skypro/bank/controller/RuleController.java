package org.skypro.bank.controller;

import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.skypro.bank.model.entity.RuleStatEntity;
import org.skypro.bank.repository.DynamicRuleRepository;
import org.skypro.bank.repository.RuleStatsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final DynamicRuleRepository repository;
    private final RuleStatsRepository statsRepository;

    public RuleController(DynamicRuleRepository repository, RuleStatsRepository statsRepository) {
        this.repository = repository;
        this.statsRepository = statsRepository;
    }

    @PostMapping
    public RecommendationRuleEntity createRule(@RequestBody RecommendationRuleEntity rule) {
        return repository.save(rule);
    }

    @GetMapping
    public Map<String, List<RecommendationRuleEntity>> getAllRules() {
        return Map.of("data", repository.findAll());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Возвращает 204
    public void deleteRule(@PathVariable UUID id) {
        repository.deleteById(id);
    }

    @GetMapping("/stats")
    public Map<String, List<Map<String, Object>>> getStats() {
        List<RecommendationRuleEntity> rules = repository.findAll();

        List<Map<String, Object>> statsList = rules.stream().map(rule -> {
            long count = statsRepository.findById(rule.getId())
                    .map(RuleStatEntity::getCount)
                    .orElse(0L);

            // Используем явное указание типов для Map.of
            // Также передаем count как число (Object это позволяет)
            return Map.<String, Object>of(
                    "rule_id", rule.getId().toString(),
                    "count", count
            );
        }).toList();

        return Map.of("stats", statsList);
    }
}