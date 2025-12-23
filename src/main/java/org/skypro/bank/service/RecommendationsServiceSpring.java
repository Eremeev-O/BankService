package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationsServiceSpring{
    private final RecommendationsRepository recommendationsRepository;
    private final RecommendationRuleSetInvest500 recommendationRuleSetInvest500;

    public RecommendationsServiceSpring(RecommendationsRepository recommendationsRepository, RecommendationRuleSetInvest500 recommendationRuleSetInvest500) {
        this.recommendationsRepository = recommendationsRepository;
        this.recommendationRuleSetInvest500 = recommendationRuleSetInvest500;
    }

    public Recomendations recomendations(UUID user){
        List<Dto> listRecom = new ArrayList<>();
        Optional<Dto> result = recommendationRuleSetInvest500.check(user);
        if (result.isPresent()) {
            listRecom.add(result.get());
        } else {
            listRecom.isEmpty();
        }
        Recomendations recom = new Recomendations(listRecom, user);
        return recom;
    }
}
