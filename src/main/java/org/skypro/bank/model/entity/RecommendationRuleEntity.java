package org.skypro.bank.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recommendation_rules")
public class RecommendationRuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Используем UUID
    private UUID id;

    @JsonProperty("product_name")
    @Column(name = "product_name")
    private String productName;

    @JsonProperty("product_id")
    @Column(name = "product_id")
    private UUID productId;

    @JsonProperty("product_text")
    @Column(name = "product_text")
    private String productText;

    @JsonProperty("rule")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "rule_id")
    private List<RuleQueryEntity> ruleQueries;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }
    public List<RuleQueryEntity> getRuleQueries() { return ruleQueries; }
    public void setRuleQueries(List<RuleQueryEntity> ruleQueries) { this.ruleQueries = ruleQueries; }
}