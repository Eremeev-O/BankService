package org.skypro.bank.controller;

import org.skypro.bank.model.Recomendations;
import org.skypro.bank.service.RecommendationsServiceSpring;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST-контроллер для получения персональных рекомендаций пользователями.
 */
@RestController
@RequestMapping("/recommendation/")
public class RecommendationsController {
    private final RecommendationsServiceSpring recommendationsServiceSpring;

    public RecommendationsController(RecommendationsServiceSpring recommendationsServiceSpring) {
        this.recommendationsServiceSpring = recommendationsServiceSpring;
    }

    @GetMapping("{user_id}")
    public Recomendations recomendations(@PathVariable UUID user_id) {
        return recommendationsServiceSpring.recomendations(user_id);
    }
}
