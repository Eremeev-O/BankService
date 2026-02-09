package org.skypro.bank.repository;

import java.util.Arrays;

/**
 * Вспомогательный класс для формирования ключа кэша.
 * Используется для идентификации уникальных запросов по методу и аргументам.
 */
record CacheKey(String methodName, Object[] args) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheKey cacheKey)) return false;
        return methodName.equals(cacheKey.methodName) && Arrays.equals(args, cacheKey.args);
    }

    @Override
    public int hashCode() {
        int result = methodName.hashCode();
        result = 31 * Arrays.hashCode(args);
        return result;
    }
}
