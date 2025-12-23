package org.skypro.bank.controller;

import org.skypro.bank.model.Recomendations;
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

    @GetMapping("{user_id}")
//    public int getRandomTransactionAmount(@PathVariable UUID uuid){
//        return recommendationsServiceSpring.getRandomTransactionAmount(uuid);
//    }
    public Recomendations recomendations(@PathVariable UUID user_id){
        return recommendationsServiceSpring.recomendations(user_id);
    }
}
