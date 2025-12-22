package org.skypro.bank.controller;

import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.*;
import org.skypro.bank.service.RecommendationsServiceSpring;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation/")
public class RecommendationsController {
    private final RecommendationsServiceSpring recommendationsServiceSpring;

    public RecommendationsController(RecommendationsServiceSpring recommendationsServiceSpring, RecommendationsRepository recommendationsRepository) {
        this.recommendationsServiceSpring = recommendationsServiceSpring;
    }

    @GetMapping("{uuid}")
//    public int getRandomTransactionAmount(@PathVariable UUID uuid){
//        return recommendationsServiceSpring.getRandomTransactionAmount(uuid);
//    }
    public String recomendations(@PathVariable UUID uuid){
        return recommendationsServiceSpring.recomendations(uuid);
    }
}
