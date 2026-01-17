package org.skypro.bank.model;

import java.util.List;
import java.util.UUID;

public class Recomendations {
    private final UUID user_id;
    private final List<Dto> recomendations;

    public Recomendations(UUID userId, List<Dto> recomendations) {
        this.user_id = userId;
        this.recomendations = recomendations;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public List<Dto> getRecomendations() {
        return recomendations;
    }

}
