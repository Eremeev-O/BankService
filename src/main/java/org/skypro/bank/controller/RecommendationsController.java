package org.skypro.bank.controller;

import org.skypro.bank.model.DTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.*;
import org.skypro.bank.service.RecommendationsServiceSpring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Map<UUID, List<DTO>> recomendations(@PathVariable UUID user_id){
        return recommendationsServiceSpring.recomendations(user_id);
    }
}
