package org.skypro.bank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean getProductCheck(UUID user, String string) {
        try {
            UUID result = jdbcTemplate.queryForObject(
                    "SELECT t.user_id FROM PUBLIC.TRANSACTIONS t\n" +
                            "\tLEFT JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID=p.ID\n" +
                            "\tWHERE t.user_id = ? AND p.TYPE = ? LIMIT 1;",
                    UUID.class,
                    user, string);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    //— USER_OF
    public boolean isUserOf(UUID user, String productType) {
        String sql = "SELECT EXISTS(" +
                "   SELECT 1 FROM PUBLIC.TRANSACTIONS t" +
                "   INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID" +
                "   WHERE t.user_id = ? AND p.TYPE = ?" +
                ")";
        return jdbcTemplate.queryForObject(sql, Boolean.class, user, productType);
    }

    //— ACTIVE_USER_OF
    public boolean isActiveUserOfProduct(UUID user, String productType) {
        String sql = "SELECT COUNT(*) >=5 FROM PUBLIC.TRANSACTIONS t " +
                "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.user_id = ? AND p.TYPE = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, user, productType);
    }

    //— TRANSACTION_SUM_COMPARE
    public boolean isTransactionsSumCompare(UUID user, String productType, String transactionType, String operator, int constant) {
        if (!isValidOperator(operator)) {
            throw new IllegalArgumentException("Оператор сравнения не поддерживается:" + operator);
        }
        String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0)" + operator + " ?" +
                "FROM PUBLIC.TRANSACTIONS t " +
                "INNER JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.user_id = ? AND p.TYPE = ? AND t.TYPE = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, constant, user, productType, transactionType);
    }

    //— TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    public boolean isTransactionSumCompareDepositWithdraw(UUID user, String productType, String operator) {
        if (!isValidOperator(operator)) {
            throw new IllegalArgumentException("Оператор сравнения не поддерживается:" + operator);
        }
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
        return compareAmounts(depositSum, withdrawSum, operator);
    }

    public int getProductSum(UUID user, String string, String cashFlow) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(t.AMOUNT) FROM PUBLIC.TRANSACTIONS t\n" +
                        "\tLEFT JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID=p.ID \n" +
                        "\tWHERE p.TYPE=? AND t.USER_ID = ? AND t.TYPE = ?;",
                Integer.class,
                string, user, cashFlow);
        return result != null ? result : 0;
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
