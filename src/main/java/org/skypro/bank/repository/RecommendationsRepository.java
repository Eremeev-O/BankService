package org.skypro.bank.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> userOfCache = createCache();
    private final Cache<String, Boolean> activeUserCache = createCache();
    private final Cache<String, Boolean> sumCompareCache = createCache();
    private final Cache<String, Boolean> dwCompareCache = createCache();

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Cache<String, Boolean> createCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public void clearAllCaches() {
        userOfCache.invalidateAll();
        activeUserCache.invalidateAll();
        sumCompareCache.invalidateAll();
        dwCompareCache.invalidateAll();
    }

    public boolean isUserOf(UUID user, String productType) {
        String key = user.toString() + ":" + productType;
        return userOfCache.get(key, k -> {
            String sql = "SELECT EXISTS(SELECT 1 FROM PUBLIC.TRANSACTIONS t " +
                    "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ?)";
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, user, productType));
        });
    }

    public boolean isActiveUserOfProduct(UUID user, String productType) {
        String key = user.toString() + ":" + productType;
        return activeUserCache.get(key, k -> {
            String sql = "SELECT COUNT(*) >= 5 FROM PUBLIC.TRANSACTIONS t " +
                    "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ?";
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, user, productType));
        });
    }

    public boolean isTransactionsSumCompare(UUID user, String productType, String transactionType, String operator, int constant) {
        String key = String.format("%s:%s:%s:%s:%d", user, productType, transactionType, operator, constant);
        return sumCompareCache.get(key, k -> {
            String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0) " + sanitizeOperator(operator) + " ? " +
                    "FROM PUBLIC.TRANSACTIONS t " +
                    "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ? AND t.TYPE = ?";
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, constant, user, productType, transactionType));
        });
    }

    public boolean isTransactionSumCompareDepositWithdraw(UUID user, String productType, String operator) {
        String key = user.toString() + ":" + productType + ":" + operator;
        return dwCompareCache.get(key, k -> {
            String sql = "SELECT " +
                    "SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) as d, " +
                    "SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) as w " +
                    "FROM PUBLIC.TRANSACTIONS t INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ?";
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    return compareAmounts(rs.getLong("d"), rs.getLong("w"), operator);
                }
                return false;
            }, user, productType);
        });
    }

    private String sanitizeOperator(String op) {
        if (op.matches(">|<|>=|<=|=")) return op;
        throw new IllegalArgumentException("Invalid operator");
    }

    private boolean compareAmounts(long d, long w, String op) {
        return switch (op) {
            case ">" -> d > w;
            case "<" -> d < w;
            case ">=" -> d >= w;
            case "<=" -> d <= w;
            case "=" -> d == w;
            default -> throw new IllegalArgumentException("Unknown operator");
        };
    }
}