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
    private final RecommendationRuleSetSimpleloan recommendationRuleSetSimpleloan;

    public RecommendationsServiceSpring(RecommendationsRepository recommendationsRepository, RecommendationRuleSetInvest500 recommendationRuleSetInvest500, RecommendationRuleSetSimpleloan recommendationRuleSetSimpleloan) {
        this.recommendationsRepository = recommendationsRepository;
        this.recommendationRuleSetInvest500 = recommendationRuleSetInvest500;
        this.recommendationRuleSetSimpleloan = recommendationRuleSetSimpleloan;
    }

    public Recomendations recomendations(UUID user){
        List<Dto> listRecom = new ArrayList<>();
        Optional<Dto> resultA = recommendationRuleSetInvest500.check(user);
        if (resultA.isPresent()) {
            listRecom.add(resultA.get());
        }
        Optional<Dto> resultB = recommendationRuleSetSimpleloan.check(user);
        if (resultB.isPresent()) {
            listRecom.add(resultB.get());
        } else {
            listRecom.isEmpty();
        }
        Recomendations recom = new Recomendations(listRecom, user);
        return recom;
    }
}
