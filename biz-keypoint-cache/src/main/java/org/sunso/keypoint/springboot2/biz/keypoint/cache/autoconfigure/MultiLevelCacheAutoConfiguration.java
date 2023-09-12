package org.sunso.keypoint.springboot2.biz.keypoint.cache.autoconfigure;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.config.MultiLevelCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CaffeineCacheRemoveListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.RedisCacheMessageListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.manager.MeRedisCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.manager.RedisAndCaffeineMultiLevelCache;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableConfigurationProperties(MultiLevelCacheConfig.class)
public class MultiLevelCacheAutoConfiguration {

    @Resource
    private MultiLevelCacheConfig multiLevelCacheConfig;

    @Bean
    @ConditionalOnMissingBean({RedisTemplate.class})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        //template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCache redisCache(RedisConnectionFactory factory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.of(multiLevelCacheConfig.getSecondLevelCacheExpireTime(), ChronoUnit.SECONDS));
        redisCacheConfiguration = redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCache redisCache = new MeRedisCache(multiLevelCacheConfig.getSecondLevelCacheName(), redisCacheWriter, redisCacheConfiguration);
        return redisCache;
    }

    @Bean
    @ConditionalOnClass(CaffeineCache.class)
    @ConditionalOnProperty(name = "cache.multilevel.firstLevelSwitch", havingValue = "true", matchIfMissing = true)
    public CaffeineCache caffeineCache() {
        CaffeineCache caffeineCache = new CaffeineCache(multiLevelCacheConfig.getFirstLevelCacheName(), Caffeine.newBuilder()
                // 设置初始缓存大小
                .initialCapacity(multiLevelCacheConfig.getFirstLevelInitCapacity())
                // 设置最大缓存
                .maximumSize(multiLevelCacheConfig.getFirstLevelMaxCapacity())
                // 设置缓存线程池
                //.executor(null)
                // 设置定时任务执行过期清除操作
                //.scheduler(Scheduler.systemScheduler())
                // 监听器(超出最大缓存)
                .removalListener(new CaffeineCacheRemoveListener())
                // 设置缓存读时间的过期时间
                .expireAfterAccess(Duration.of(multiLevelCacheConfig.getFirstLevelCacheExpireTime(), ChronoUnit.SECONDS))
                // 开启metrics监控
                .recordStats()
                .build());
        return caffeineCache;
    }

    @Bean
    @ConditionalOnBean({RedisCache.class, CaffeineCache.class})
    public RedisAndCaffeineMultiLevelCache redisAndCaffeineMultiLevelCache(RedisCache redisCache, CaffeineCache caffeineCache) {
        return new RedisAndCaffeineMultiLevelCache(true, redisCache, caffeineCache);
    }

    @Bean
    public RedisCacheMessageListener redisCacheMessageListener(CaffeineCache caffeineCache) {
        RedisCacheMessageListener redisCacheMessageListener = new RedisCacheMessageListener();
        redisCacheMessageListener.setCaffeineCache(caffeineCache);
        return redisCacheMessageListener;
    }

    @Bean
    @ConditionalOnMissingBean({RedisMessageListenerContainer.class})
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       RedisCacheMessageListener redisCacheMessageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(redisCacheMessageListener, new ChannelTopic(multiLevelCacheConfig.getCacheName()+"_topic"));
        return redisMessageListenerContainer;
    }
}
