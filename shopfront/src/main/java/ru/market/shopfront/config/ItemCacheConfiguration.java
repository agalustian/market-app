package ru.market.shopfront.config;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.market.shopfront.models.Item;

@Configuration
public class ItemCacheConfiguration {

  @Bean
  public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
    ObjectMapper cacheObjectMapper = objectMapper.copy();
    cacheObjectMapper.activateDefaultTyping(cacheObjectMapper.getPolymorphicTypeValidator(),
        ObjectMapper.DefaultTyping.NON_FINAL,
        PROPERTY);
    Jackson2JsonRedisSerializer<Item> itemSerializer =
        new Jackson2JsonRedisSerializer<>(cacheObjectMapper, Item.class);

    Jackson2JsonRedisSerializer<Item[]> itemsSerializer =
        new Jackson2JsonRedisSerializer<>(cacheObjectMapper, Item[].class);

    return builder -> builder.cacheDefaults(defaultCacheConfig()
        .serializeValuesWith(fromSerializer(itemSerializer))
        .disableCachingNullValues()
        .entryTtl(Duration.of(30, ChronoUnit.SECONDS)))
        .withCacheConfiguration("items", defaultCacheConfig()
            .serializeValuesWith(fromSerializer(itemsSerializer))
            .disableCachingNullValues()
            .entryTtl(Duration.of(60, ChronoUnit.SECONDS)));
  }

}

