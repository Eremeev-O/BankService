package org.skypro.bank.repository;

import java.util.Arrays;

record CacheKey(String methodName, Object[] args) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheKey)) return false;
        CacheKey cacheKey = (CacheKey) o;
        return methodName.equals(cacheKey.methodName) && Arrays.equals(args, cacheKey.args);
    }

    @Override
    public int hashCode() {
        int result = methodName.hashCode();
        result = 31 * Arrays.hashCode(args);
        return result;
    }
}
