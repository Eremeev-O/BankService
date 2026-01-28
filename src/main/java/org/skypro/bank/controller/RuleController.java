package org.skypro.bank.controller;

import org.skypro.bank.model.entity.RecommendationRuleEntity;
import org.skypro.bank.repository.DynamicRuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final DynamicRuleRepository repository;

    public RuleController(DynamicRuleRepository repository) {
        this.repository = repository;
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
}