package org.skypro.bank.model;

import java.util.List;
import java.util.UUID;

public class Recomendations {
    private final UUID user_id;
    private final List<DTO> recomendations;

    public Recomendations(List<DTO> recomendations, UUID userId) {
        this.user_id = userId;
        this.recomendations = recomendations;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public List<DTO> getRecomendations() {
        return recomendations;
    }

}
