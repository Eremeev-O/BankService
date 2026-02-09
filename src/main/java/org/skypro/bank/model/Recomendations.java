package org.skypro.bank.model;

import java.util.List;
import java.util.UUID;

/**
 * Результирующий набор рекомендаций для пользователя.
 *
 * @param user_id идентификатор пользователя
 * @param recomendations список подходящих продуктов
 */
public record Recomendations(UUID user_id, List<Dto> recomendations) {

}
