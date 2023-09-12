package org.sunso.keypoint.springboot2.biz.keypoint.cache.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.config.MultiLevelCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CacheMessage;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.Callable;


@Slf4j
public class RedisAndCaffeineMultiLevelCache extends AbstractValueAdaptingCache {

    @Resource
    private MultiLevelCacheConfig multiLevelCacheConfig;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private RedisCache redisCache;
    private CaffeineCache caffeineCache;

    public RedisAndCaffeineMultiLevelCache(boolean allowNullValues, RedisCache redisCache, CaffeineCache caffeineCache) {
        super(allowNullValues);
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
    }

    @Override
    protected Object lookup(Object key) {
        Assert.notNull(key, "key不可为空");
        ValueWrapper value;
        //一级缓存开启，先从一级缓存获取数据
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            value = caffeineCache.get(key);
            if (Objects.nonNull(value)) {
                log.info("RedisAndCaffeineMultiLevelCache get key[{}] cache value[{}] from first level cache", key ,value);
                return value.get();
            }
        }
        // 从二级缓存获取数据
        value = redisCache.get(key);
        if (Objects.isNull(value)) {
            log.info("RedisAndCaffeineMultiLevelCache get key[{}] cache value is null from second level cache", key);
            return null;
        }
        log.info("RedisAndCaffeineMultiLevelCache get key[{}] cache value[{}] from second level cache", key, value);
        // 如果开启了一级缓存，就把二级缓存数据写入到一级缓存中
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            log.info("RedisAndCaffeineMultiLevelCache set key[{}] cache value[{}] to first level cache", key, value);
            caffeineCache.put(key, value.get());
        }
        return value.get();
    }

    @Override
    public String getName() {
        return multiLevelCacheConfig.getCacheName();
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        return (T)lookup(key);
    }

    @Override
    public void put(Object key, Object value) {
        redisCache.put(key, value);
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            asyncPublish(key, value);
        }
    }

    /**
     * key不存在时，再保存，存在返回当前值不覆盖
     * @param key
     * @param value
     * @return
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = redisCache.putIfAbsent(key, value);
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            asyncPublish(key, value);
        }
        return valueWrapper;
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            asyncPublish(key, null);
        }
    }

    @Override
    public void clear() {
        redisCache.clear();
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            asyncPublish(null, null);
        }
    }

    void asyncPublish(Object key, Object value) {
        CacheMessage message = getCacheMessage(key, value);
        redisTemplate.convertAndSend(message.getChannel(), message);
        log.info("asyncPublish message[{}] to channel[{}]", message, message.getChannel());
    }

    private CacheMessage getCacheMessage(Object key, Object value) {
        CacheMessage message = new CacheMessage();
        message.setCacheName(multiLevelCacheConfig.getCacheName());
        message.setKey(key);
        message.setValue(value);
        return message;
    }
}
