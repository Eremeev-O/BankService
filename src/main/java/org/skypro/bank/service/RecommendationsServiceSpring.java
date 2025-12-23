package org.skypro.bank.service;

import org.skypro.bank.model.DTO;
import org.skypro.bank.model.Recomendations;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationsServiceSpring implements RecommendationRuleSetInvest500{
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationsServiceSpring(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public Recomendations recomendations(UUID user){
        List<DTO> listRecom = new ArrayList<>();
        Optional<DTO> result = recomendationsInvest500(user);
        if (result.isPresent()) {
            listRecom.add(result.get());
        } else {
            listRecom.isEmpty();
        }
        Recomendations recom = new Recomendations(listRecom, user);
        return recom;
    }

    @Override
    public Optional<DTO>  recomendationsInvest500(UUID user) {
        Boolean result = recommendationsRepository.getProductDebitOrInvest(user, "DEBIT");
        Boolean result1 = !recommendationsRepository.getProductDebitOrInvest(user, "INVEST");
        Boolean result2 = recommendationsRepository.getProductSumSAVING(user)>1000;
        if(result && result1 && result2) {
            UUID id = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
            String recom = "Invest 500";
            String text = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
            Optional<DTO> recomendation = Optional.of(new DTO("Invest 500", id, text));
            return recomendation;
        }
        return Optional.empty();
    }
}
