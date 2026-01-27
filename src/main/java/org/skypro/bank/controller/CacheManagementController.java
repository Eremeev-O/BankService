package org.skypro.bank.controller;

import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class CacheManagementController {
    private final RecommendationsRepository recommendationsRepository;

    public CacheManagementController(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        recommendationsRepository.clearAllCaches();
        return ResponseEntity.noContent().build();
    }
}
