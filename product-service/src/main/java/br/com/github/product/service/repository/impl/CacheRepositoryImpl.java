package br.com.github.product.service.repository.impl;

import br.com.github.product.service.repository.CacheRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class CacheRepositoryImpl implements CacheRepository {

    @Value("${spring.cache.redis.key-prefix}")
    private String keyPrefix;

    private static final Logger logger = Logger.getLogger(CacheRepositoryImpl.class.getName());

    private final ObjectMapper mapper;
    private final ValueOperations<String, Object> valueOps;
    private final ListOperations<String, Object> listOps;


    public CacheRepositoryImpl(final ObjectMapper mapper, final RedisTemplate<String, Object> redisTemplate) {
        this.mapper = mapper;
        this.valueOps = redisTemplate.opsForValue();
        this.listOps = redisTemplate.opsForList();
    }

    public void setCache(final String key, final Object data) {
        try {
            if (data != null) {
                valueOps.set(this.keyPrefix + ":" + key, data, 10L, TimeUnit.MINUTES);
            }
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    public <T> T getCachedValue(final String key, Class<T> type) {
        try {
            return converter(valueOps.get(this.keyPrefix + ":" + key), type);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }

    public void deleteCachedValue(final String key) {
        valueOps.getOperations().delete(key);
    }

    public void setCache(final String key, final List<Object> data) {
        for (final Object d : data) {
            listOps.leftPush(key, d);
        }
    }

    private <T> T converter(Object value, Class<T> type) {
        if (value != null) {
            return mapper.convertValue(value, type);
        }
        return null;
    }

}
