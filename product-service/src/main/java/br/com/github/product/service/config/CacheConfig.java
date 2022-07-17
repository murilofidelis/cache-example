package br.com.github.product.service.config;

import br.com.github.product.service.dto.CategoryDTO;
import br.com.github.product.service.dto.CategoryFilterDTO;
import br.com.github.product.service.properties.RedisProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    private static final Logger logger = Logger.getLogger(CacheConfig.class.getName());

    @Profile("!dev")
    @Bean("connectionFactory")
    public LettuceConnectionFactory connectionFactory(RedisProperties properties) {
        final LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(1000L)).build();
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());
        config.setPassword(properties.getPassword());
        config.setDatabase(0);
        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(config, clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }

    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean("categoryKeyGenerator")
    public KeyGenerator categoryKeyGenerator() {
        return (o, method, objects) -> String.format("%s", objects[0]);
    }

    @Bean("categoryUpdateKeyGenerator")
    public KeyGenerator categoryUpdateKeyGenerator() {
        return (o, method, objects) -> {
            var category = (CategoryDTO) objects[0];
            return String.format("%s", category.getId());
        };
    }

    @Bean("categoriesFilterKeyGenerator")
    public KeyGenerator categoriesFilterKeyGenerator() {
        return (o, method, objects) -> {
            var filter = (CategoryFilterDTO) objects[0];
            return String.format("status:%s", filter.isStatus());
        };
    }

    @Bean(name = "myCustomCacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        var categoryCacheManager = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(CategoryDTO.class)))
                .prefixKeysWith("myCache::category:");

        var categoryUpdateCacheManager = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(CategoryDTO.class)))
                .prefixKeysWith("myCache::category:");

        var categoriesCacheManager = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(List.class)))
                .prefixKeysWith("myCache::categories:");

        var categoriesFilterCacheManager = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(List.class)))
                .prefixKeysWith("myCache::categoriesFilter:");

        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        map.put("category", categoryCacheManager);
        map.put("categoryUpdate", categoryUpdateCacheManager);
        map.put("categories", categoriesCacheManager);
        map.put("categoriesFilter", categoriesFilterCacheManager);

        return RedisCacheManager.RedisCacheManagerBuilder.fromCacheWriter(redisCacheWriter)
                .withInitialCacheConfigurations(map)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                logger.log(Level.SEVERE, "Failure getting from cache: {0}", cache.getName());
                logger.log(Level.SEVERE, "{}", exception.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                logger.log(Level.SEVERE, "Failure putting into cache: {0}", cache.getName());
                logger.log(Level.SEVERE, "{}", exception.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                logger.log(Level.SEVERE, "Failure evicting from cache: {0}", cache.getName());
                logger.log(Level.SEVERE, "{}", exception.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                logger.log(Level.SEVERE, "Failure clearing cache: {0}", cache.getName());
                logger.log(Level.SEVERE, "{}", exception.toString());
            }
        };
    }


}
