package org.skypro.bank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean getProductCheck(UUID user, String string){
        try {
            UUID result = jdbcTemplate.queryForObject(
                    "SELECT t.user_id FROM PUBLIC.TRANSACTIONS t\n" +
                            "\tLEFT JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID=p.ID\n" +
                            "\tWHERE t.user_id = ? AND p.TYPE = ? LIMIT 1;",
                    UUID.class,
                    user, string);
            return true;
        }
        catch (RuntimeException e){
            return false;
        }
    }

    public int getProductSum(UUID user, String string, String cashFlow){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(t.AMOUNT) FROM PUBLIC.TRANSACTIONS t\n" +
                        "\tLEFT JOIN PUBLIC.PRODUCTS p ON t.PRODUCT_ID=p.ID \n" +
                        "\tWHERE p.TYPE=? AND t.USER_ID = ? AND t.TYPE = ?;",
                Integer.class,
                string, user, cashFlow);
        return result != null ? result : 0;
    }

}
