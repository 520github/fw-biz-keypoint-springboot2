package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CaffeineCacheRemoveListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LocalCaffeineCache extends AbstractLocalCache {
    private static Map<String, CaffeineCache> caffeineCacheMap = new ConcurrentHashMap<>();

    public LocalCaffeineCache(LocalCacheModel localCacheModel) {
        super(localCacheModel);
    }

    @Override
    public Object get(String key, long expireTime, TimeUnit timeUnit) {
        Cache.ValueWrapper valueWrapper = getCaffeineCache(expireTime, timeUnit).get(key);
        if (valueWrapper != null) {
            return valueWrapper.get();
        }
        return null;
    }

    @Override
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        getCaffeineCache(expireTime, timeUnit).put(key, value);
    }

    @Override
    public int remove(String key) {
        for(String cacheKey: caffeineCacheMap.keySet()) {
            CaffeineCache caffeineCache = caffeineCacheMap.get(cacheKey);
            caffeineCache.evict(key);
        }
        return 1;
    }

    @Override
    public int remove(String key, long expireTime, TimeUnit timeUnit) {
        String cacheName = getCacheName(expireTime, timeUnit);
        CaffeineCache caffeineCache = caffeineCacheMap.get(cacheName);
        if (caffeineCache == null) {
            return 0;
        }
        caffeineCache.evict(key);
        return 1;
    }

    private CaffeineCache getCaffeineCache(long expireTime, TimeUnit timeUnit) {
        String cacheName = getCacheName(expireTime, timeUnit);
        CaffeineCache caffeineCache = caffeineCacheMap.get(cacheName);
        if (caffeineCache == null) {
            caffeineCache = newCaffeineCache(expireTime, timeUnit);
            caffeineCacheMap.put(cacheName, caffeineCache);
        }
        return caffeineCache;
    }

    private CaffeineCache newCaffeineCache(long expireTime, TimeUnit timeUnit) {
        if (!isGroupByExpireTime()) {
            return defaultCaffeineCache();
        }
        return newOneCaffeineCache(expireTime, timeUnit);
    }

    private CaffeineCache newOneCaffeineCache(long expireTime, TimeUnit timeUnit) {
        return new CaffeineCache(getCacheName(expireTime, timeUnit), Caffeine.newBuilder()
                // 设置初始缓存大小
                .initialCapacity(getInitCapacity())
                // 设置最大缓存
                .maximumSize(getMaxCapacity())
                // 设置缓存线程池
                //.executor(null)
                // 设置定时任务执行过期清除操作
                //.scheduler(Scheduler.systemScheduler())
                // 监听器(超出最大缓存)
                .removalListener(new CaffeineCacheRemoveListener())
                // 设置缓存写入后的过期时间
                .expireAfterWrite(expireTime, timeUnit)
                // 开启metrics监控
                .recordStats()
                .build());
    }

    @Override
    protected String cacheType() {
        return "caffeine";
    }

    private CaffeineCache defaultCaffeineCache() {
        return newOneCaffeineCache(getDefaultExpireTime(), getDefaultTimeUnit());
    }
}
