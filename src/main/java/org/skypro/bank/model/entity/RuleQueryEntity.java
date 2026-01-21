package org.skypro.bank.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rule_queries")
public class RuleQueryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id"))
    @OrderColumn
    private List<String> arguments;

    private boolean negate;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public List<String> getArguments() { return arguments; }
    public void setArguments(List<String> arguments) { this.arguments = arguments; }
    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }
}