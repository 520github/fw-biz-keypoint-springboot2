package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.constant.Constants;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.CustomMultiLevelCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.listener.MultiLevelCacheMessageListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.RedisAndCaffeineMultiLevelCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.config.MultiLevelCacheConfig;
import org.sunso.keypoint.springboot2.spring.SpringEnvironment;

import javax.annotation.Resource;

@Slf4j
@Configuration
@EnableConfigurationProperties(MultiLevelCacheConfig.class)
public class MultiLevelCacheAutoConfiguration {
    @Resource
    private MultiLevelCacheConfig multiLevelCacheConfig;

    @Bean
    @ConditionalOnMissingBean({SpringEnvironment.class})
    public SpringEnvironment springEnvironment() {
        return new SpringEnvironment();
    }

    @Bean(Constants.METHOD_CACHE_REDIS_TEMPLATE)
    @ConditionalOnMissingBean(name = Constants.METHOD_CACHE_REDIS_TEMPLATE)
    public RedisTemplate<String, Object> methodCacheRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        //template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        log.info("redisTemplate KeySerializer[{}]", template.getKeySerializer().getClass().getName());
        return template;
    }

    @Bean
    public RedisAndCaffeineMultiLevelCache redisAndCaffeineMultiLevelCache() {
        return new RedisAndCaffeineMultiLevelCache(multiLevelCacheConfig);
    }

    @Bean
    public CustomMultiLevelCache customMultiLevelCache() {
        LocalCache localCache = LocalCacheManager.getCache(
                LocalCacheTypeEnum.getByCacheType(multiLevelCacheConfig.getFirstLevelCacheType()), LocalCacheModel.newInstance(multiLevelCacheConfig));
        DistributeCache distributeCache = DistributeCacheManager.getCache(
                DistributeCacheTypeEnum.getByCacheType(multiLevelCacheConfig.getSecondFirstLevelCacheType()));
        return new CustomMultiLevelCache(distributeCache, localCache, multiLevelCacheConfig);
    }

    @Bean
    public MultiLevelCacheMessageListener multiLevelCacheMessageListener() {
        MultiLevelCacheMessageListener multiLevelCacheMessageListener = new MultiLevelCacheMessageListener();
        return multiLevelCacheMessageListener;
    }

    @Bean
    @ConditionalOnMissingBean({RedisMessageListenerContainer.class})
    public RedisMessageListenerContainer multiLevelMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       MultiLevelCacheMessageListener multiLevelCacheMessageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(multiLevelCacheMessageListener, new ChannelTopic(Constants.MULTI_LEVEL_CACHE_REDIS_CHANNEL));
        return redisMessageListenerContainer;
    }

    //    @Bean
//    public RedisCache redisCache(RedisConnectionFactory factory) {
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
//        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.of(multiLevelCacheConfig.getSecondLevelCacheExpireTime(), ChronoUnit.SECONDS));
//        redisCacheConfiguration = redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
//        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//        RedisCache redisCache = new MeRedisCache(multiLevelCacheConfig.getSecondLevelCacheName(), redisCacheWriter, redisCacheConfiguration);
//        return redisCache;
//    }

//    @Bean
//    @ConditionalOnClass(CaffeineCache.class)
//    @ConditionalOnProperty(name = "cache.multilevel.firstLevelSwitch", havingValue = "true", matchIfMissing = true)
//    public CaffeineCache caffeineCache() {
//        CaffeineCache caffeineCache = new CaffeineCache(multiLevelCacheConfig.getFirstLevelCacheName(), Caffeine.newBuilder()
//                // 设置初始缓存大小
//                .initialCapacity(multiLevelCacheConfig.getFirstLevelInitCapacity())
//                // 设置最大缓存
//                .maximumSize(multiLevelCacheConfig.getFirstLevelMaxCapacity())
//                // 设置缓存线程池
//                //.executor(null)
//                // 设置定时任务执行过期清除操作
//                //.scheduler(Scheduler.systemScheduler())
//                // 监听器(超出最大缓存)
//                .removalListener(new CaffeineCacheRemoveListener())
//                // 设置缓存读时间的过期时间
//                .expireAfterAccess(Duration.of(multiLevelCacheConfig.getFirstLevelCacheExpireTime(), ChronoUnit.SECONDS))
//                // 开启metrics监控
//                .recordStats()
//                .build());
//        return caffeineCache;
//    }
}
