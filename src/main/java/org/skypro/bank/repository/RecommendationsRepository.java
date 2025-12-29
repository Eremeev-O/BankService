package org.skypro.bank.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final Cache<CacheKey, Boolean> activeUserOfCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            //.recordStats()
            .build();

    private final Cache<CacheKey, Boolean> transactionsSumCompare = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            //.recordStats()
            .build();

    private final Cache<CacheKey, Boolean> transactionSumCompareDepositWithdraw = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            //.recordStats()
            .build();

    //— 1 USER_OF
    public boolean isUserOf(UUID user, String productType) {
        String sql = "SELECT EXISTS(" +
                "   SELECT 1 FROM PUBLIC.TRANSACTIONS t" +
                "   INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID" +
                "   WHERE t.user_id = ? AND p.TYPE = ?" +
                ")";
        return jdbcTemplate.queryForObject(sql, Boolean.class, user, productType);
    }

    //— 2 ACTIVE_USER_OF
    public boolean isActiveUserOfProduct(UUID user, String productType) {
        CacheKey key = new CacheKey("isActiveUserOfProduct", new Object[]{user, productType});

        return activeUserOfCache.get(key, k -> {
            String sql = "SELECT COUNT(*) >=5 FROM PUBLIC.TRANSACTIONS t " +
                    "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ?";
            return jdbcTemplate.queryForObject(sql, Boolean.class, user, productType);
        });
    }

    //— 3 TRANSACTION_SUM_COMPARE
    public boolean isTransactionsSumCompare(UUID user, String productType, String transactionType, String operator, int constant) {
        if (!isValidOperator(operator)) {
            throw new IllegalArgumentException("Оператор сравнения не поддерживается:" + operator);
        }
        CacheKey key = new CacheKey("isTransactionsSumCompare", new Object[]{user, productType, transactionType, operator, constant});

        return transactionsSumCompare.get(key, k -> {
            String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0)" + operator + " ?" +
                    "FROM PUBLIC.TRANSACTIONS t " +
                    "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.user_id = ? AND p.TYPE = ? AND t.TYPE = ?";
            return jdbcTemplate.queryForObject(sql, Boolean.class, constant, user, productType, transactionType);
        });
    }

    //— 4 TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    public boolean isTransactionSumCompareDepositWithdraw(UUID user, String productType, String operator) {
        if (!isValidOperator(operator)) {
            throw new IllegalArgumentException("Оператор сравнения не поддерживается:" + operator);
        }
        CacheKey key = new CacheKey("isTransactionSumCompareDepositWithdraw", new Object[]{user, productType, operator});

        return transactionSumCompareDepositWithdraw.get(key, k -> {
            String sql = """
                    SELECT
                        SUM(CASE WHEN t.TYPE = 'DEPOSIT'  THEN t.AMOUNT ELSE 0 END) AS deposit_sum,
                        SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS withdraw_sum
                    FROM PUBLIC.TRANSACTIONS t
                    INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID
                    WHERE t.user_id = ? AND p.TYPE = ?""";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, user, productType);

            Long depositSum = (Long) result.get("deposit_sum");
            Long withdrawSum = (Long) result.get("withdraw_sum");
            if (depositSum == null) {
                depositSum = 0L;
            }
            if (withdrawSum == null) {
                withdrawSum = 0L;
            }
            return compareAmounts(depositSum, withdrawSum, operator);
        });
    }

    private boolean isValidOperator(String op) {
        return List.of(">", "<", "=", ">=", "<=").contains(op);
    }

    private boolean compareAmounts(Long depositSum, Long withdrawSum, String operator) {
        switch (operator) {
            case ">":
                return depositSum.compareTo(withdrawSum) > 0;
            case "<":
                return depositSum.compareTo(withdrawSum) < 0;
            case "=":
                return depositSum.compareTo(withdrawSum) == 0;
            case ">=":
                return depositSum.compareTo(withdrawSum) >= 0;
            case "<=":
                return depositSum.compareTo(withdrawSum) <= 0;
            default:
                throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }
}
