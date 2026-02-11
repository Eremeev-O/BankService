package org.skypro.bank.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Объект передачи данных, представляющий банковский продукт в рекомендациях.
 *
 * @param name название продукта
 * @param id уникальный идентификатор продукта
 * @param text маркетинговое описание продукта
 */
public record BankProductDto(String name, UUID id, String text) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankProductDto bankProductDto = (BankProductDto) o;
        return Objects.equals(name, bankProductDto.name) && Objects.equals(text, bankProductDto.text);
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
