package org.skypro.bank.model;

import java.util.Objects;
import java.util.UUID;

public class Dto {
    private final UUID id;
    private final String name;
    private final String text;

    public Dto(String name, UUID id, String text) {
//        this.id = UUID.randomUUID();
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dto dto = (Dto) o;
        return Objects.equals(name, dto.name) && Objects.equals(text, dto.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text);
    }

    @Override
    public String toString() {
        return "name :" + name + ", id : " + id + ", text : " + text;
    }
}
