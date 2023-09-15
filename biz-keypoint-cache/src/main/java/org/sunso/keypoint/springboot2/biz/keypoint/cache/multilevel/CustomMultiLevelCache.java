package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CacheMessage;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.ExpireTimeModel;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.config.MultiLevelCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.notify.LocalCacheRemoveNotify;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomMultiLevelCache implements MultiLevelCache {
    private MultiLevelCacheConfig multiLevelCacheConfig;
    private DistributeCache distributeCache;
    private LocalCache localCache;
    @Resource
    private LocalCacheRemoveNotify localCacheRemoveNotify;

    //private Map<String, ExpireTimeModel> keyExpireTimeMap = new HashMap<>();

    public CustomMultiLevelCache(DistributeCache distributeCache, LocalCache localCache, MultiLevelCacheConfig multiLevelCacheConfig) {
        this.distributeCache = distributeCache;
        this.localCache = localCache;
        this.multiLevelCacheConfig = multiLevelCacheConfig;
    }

//    public CustomMultiLevelCache setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        return this;
//    }

//    @Override
//    public Object get(String key) {
//        return get(key, multiLevelCacheConfig.getCacheDefaultExpireTime(), TimeUnit.SECONDS);
//    }

    @Override
    public Object get(String key, long expireTime, TimeUnit timeUnit) {
        key = getMultiLevelKey(key);
        //一级缓存开启，先从一级缓存获取数据
        Object value;
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            value = localCache.get(key, expireTime, timeUnit);
            if (Objects.nonNull(value)) {
                log.info("CustomMultiLevelCache get key[{}] cache value[{}] from first level cache", key ,value);
                return value;
            }
        }
        // 从二级缓存获取数据
        value = distributeCache.get(key);
        if (Objects.isNull(value)) {
            log.info("CustomMultiLevelCache get key[{}] cache value is null from second level cache", key);
            return null;
        }
        log.info("CustomMultiLevelCache get key[{}] cache value[{}] from second level cache", key, value);
        // 如果开启了一级缓存，就把二级缓存数据写入到一级缓存中
        if (multiLevelCacheConfig.isFirstLevelSwitch()) {
            log.info("CustomMultiLevelCache set key[{}] cache value[{}] to first level cache", key, value);
            localCache.set(key, value, expireTime, timeUnit);
        }
        return value;
    }

    @Override
    public <T> T get(String key, long expireTime, TimeUnit timeUnit, Class<T> tClass) {
        Object result = get(key, expireTime, timeUnit);
        if (result == null) {
            return null;
        }
        return (T)result;
    }

    @Override
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        key = getMultiLevelKey(key);
        distributeCache.set(key, value, expireTime, timeUnit);
        localCache.set(key, value, expireTime, timeUnit);
        //keyExpireTimeMap.put(key, ExpireTimeModel.newInstance(expireTime, timeUnit));
        notifyLocalCacheRemove(key, value, expireTime, timeUnit);
    }

    @Override
    public int remove(String key) {
        key = getMultiLevelKey(key);
        distributeCache.remove(key);
        localCache.remove(key);
        notifyLocalCacheRemove(key, null, null, null);
        return 1;
    }

    @Override
    public int remove(String key, long expireTime, TimeUnit timeUnit) {
        key = getMultiLevelKey(key);
        distributeCache.remove(key);
        localCache.remove(key, expireTime, timeUnit);
        notifyLocalCacheRemove(key, null, expireTime, timeUnit);
        return 1;
    }

    @Override
    public int clear() {
        localCache.clear();
        notifyLocalCacheRemove(null, null, null, null);
        return 1;
    }

    private String getMultiLevelKey(String key) {
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(multiLevelCacheConfig.getBizServiceCachePrefixKey())) {
            sb.append(multiLevelCacheConfig.getBizServiceCachePrefixKey());
        }
        if (StrUtil.isNotBlank(multiLevelCacheConfig.getMultiLevelCachePrefixKey())) {
            sb.append(multiLevelCacheConfig.getMultiLevelCachePrefixKey());
        }
        sb.append(key);
        return sb.toString();
    }

    private void notifyLocalCacheRemove(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        if (!multiLevelCacheConfig.isFirstLevelSwitch()) {
            return;
        }
        asyncPublish(key, value, expireTime, timeUnit);
    }
    void asyncPublish(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        CacheMessage message = getCacheMessage(key, value, expireTime, timeUnit);
        localCacheRemoveNotify.notifyLocalCacheRemove(message);
        log.info("asyncPublish message[{}] to channel[{}]", message, message.getChannel());
    }

    private CacheMessage getCacheMessage(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        CacheMessage message = new CacheMessage();
        message.setCacheName(multiLevelCacheConfig.getCacheName());
        message.setLocalCacheType(localCache.cacheType());
        message.setLocalCacheGroupByExpireTime(multiLevelCacheConfig.isFirstLevelGroupByExpireTime());
        message.setKey(key);
        message.setValue(value);
        message.setExpireTime(expireTime);
        message.setTimeUnit(timeUnit);
        return message;
    }
}
