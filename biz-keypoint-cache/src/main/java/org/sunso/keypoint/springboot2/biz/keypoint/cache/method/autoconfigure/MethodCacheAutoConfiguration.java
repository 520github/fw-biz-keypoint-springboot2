package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.constant.Constants;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.aspect.MethodCacheAspect;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.config.MethodCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.controller.MethodCacheController;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.notify.LocalCacheRemoveNotify;

@Slf4j
@Configuration
@EnableConfigurationProperties(MethodCacheConfig.class)
public class MethodCacheAutoConfiguration {

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
    public MethodCacheAspect methodCacheAspect() {
        return new MethodCacheAspect();
    }

    @Bean
    public MethodCacheController methodCacheController() {
        return new MethodCacheController();
    }

    @Bean
    public LocalCacheRemoveNotify localCacheRemoveNotify(RedisTemplate methodCacheRedisTemplate) {
        return new LocalCacheRemoveNotify(methodCacheRedisTemplate);
    }
}
