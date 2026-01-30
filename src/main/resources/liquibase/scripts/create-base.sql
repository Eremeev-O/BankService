-- liquibase formatted sql

-- changeset ProteZ63:1
CREATE TABLE recommendation_rules (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255),
    product_id UUID,
    product_text TEXT
);

CREATE TABLE rule_queries (
    id SERIAL PRIMARY KEY,
    rule_id UUID REFERENCES recommendation_rules(id) ON DELETE CASCADE,
    query VARCHAR(100),
    negate BOOLEAN
);

CREATE TABLE query_arguments (
    query_id INTEGER REFERENCES rule_queries(id) ON DELETE CASCADE,
    arguments VARCHAR(255),
    arguments_order INTEGER
);

-- changeset ProteZ63:2
CREATE TABLE rule_stats (
    rule_id UUID PRIMARY KEY REFERENCES recommendation_rules(id) ON DELETE CASCADE,
    count BIGINT DEFAULT 0
);