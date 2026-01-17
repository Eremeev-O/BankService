package org.skypro.bank.service;

import org.skypro.bank.model.Dto;
import org.skypro.bank.model.Recomendations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationsServiceSpring {
    private final RecommendationRuleSetInvest500 recommendationRuleSetInvest500;
    private final RecommendationRuleSetSimpleloan recommendationRuleSetSimpleloan;
    private final RecommendationRuleSetTopSaving recommendationRuleSetTopSaving;

    public RecommendationsServiceSpring(RecommendationRuleSetInvest500 recommendationRuleSetInvest500, RecommendationRuleSetSimpleloan recommendationRuleSetSimpleloan, RecommendationRuleSetTopSaving recommendationRuleSetTopSaving) {
        this.recommendationRuleSetInvest500 = recommendationRuleSetInvest500;
        this.recommendationRuleSetSimpleloan = recommendationRuleSetSimpleloan;
        this.recommendationRuleSetTopSaving = recommendationRuleSetTopSaving;
    }

    public Recomendations recomendations(UUID user) {
        List<Dto> listRecom = new ArrayList<>();

        Optional<Dto> resultA = recommendationRuleSetInvest500.check(user);
        resultA.ifPresent(listRecom::add);

        Optional<Dto> resultB = recommendationRuleSetSimpleloan.check(user);
        resultB.ifPresent(listRecom::add);

        Optional<Dto> resultC = recommendationRuleSetTopSaving.check(user);
        resultC.ifPresent(listRecom::add);

        return new Recomendations(user, listRecom);
    }
}
