package org.skypro.bank.controller.management;

import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления кэшем приложения.
 * Используется в технических целях (management).
 */
@RestController
@RequestMapping("/management")
public class CacheManagementController {
    private final RecommendationsRepository recommendationsRepository;

    public CacheManagementController(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @PostMapping("/clear-caches")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCaches() {
        recommendationsRepository.clearAllCaches();
    }
}
