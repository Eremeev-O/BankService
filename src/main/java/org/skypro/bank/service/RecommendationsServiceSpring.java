package org.skypro.bank.service;

import org.skypro.bank.model.DTO;
import org.skypro.bank.repository.RecommendationsRepository;
import org.springframework.scheduling.support.DefaultScheduledTaskObservationConvention;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationsServiceSpring implements RecommendationRuleSetInvest500{
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationsServiceSpring(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

//    public int getRandomTransactionAmount(UUID user){
//        return recommendationsRepository.getRandomTransactionAmount(user);
//    }

    public List<DTO> recomendations(UUID user){
//        Boolean result = recommendationsRepository.getProductDebitOrInvest(user, "DEBIT");
//        Boolean result1 = !recommendationsRepository.getProductDebitOrInvest(user, "INVEST");
//        Boolean result2 = recommendationsRepository.getProductSumSAVING(user)>1000;
        if(result && result1 && result2) {
            return "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
        }
    return "Рекомендовать не чего";
    }

    @Override
    public Optional<DTO>  recomendationsInvest500(UUID user) {
        Boolean result = recommendationsRepository.getProductDebitOrInvest(user, "DEBIT");
        Boolean result1 = !recommendationsRepository.getProductDebitOrInvest(user, "INVEST");
        Boolean result2 = recommendationsRepository.getProductSumSAVING(user)>1000;
        if(result && result1 && result2) {
            String recom = "Invest 500";
            String text = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
            Optional<DTO> recomendation = new Optional<DTO>("Invest 500", text);
            return recomendation;
        }
        return Optional.empty();
    }
}
