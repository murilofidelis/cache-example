package br.com.github.product.service.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository {

    void setCache(final String key, final Object data);

    <T> T getCachedValue(final String key, Class<T> type);

    void deleteCachedValue(final String key);
}
