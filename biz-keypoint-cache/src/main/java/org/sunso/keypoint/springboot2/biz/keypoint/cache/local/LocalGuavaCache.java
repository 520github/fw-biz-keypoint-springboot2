package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.GuavaCacheRemoveListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LocalGuavaCache extends AbstractLocalCache {
    private static Map<String, LoadingCache> guavaCacheMap = new ConcurrentHashMap<>();

    public LocalGuavaCache(LocalCacheModel localCacheModel) {
        super(localCacheModel);
    }

    @Override
    protected String cacheType() {
        return "guava";
    }

    @SneakyThrows
    @Override
    public Object get(String key, long expireTime, TimeUnit timeUnit) {
        return getGuavaCache(expireTime, timeUnit).get(key);
    }

    @Override
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        getGuavaCache(expireTime, timeUnit).put(key, value);
    }

    @Override
    public int remove(String key) {
        for(String cacheKey: guavaCacheMap.keySet()) {
            LoadingCache guavaCache = guavaCacheMap.get(cacheKey);
            guavaCache.invalidate(key);
        }
        return 1;
    }

    @Override
    public int remove(String key, long expireTime, TimeUnit timeUnit) {
        String cacheName = getCacheName(expireTime, timeUnit);
        LoadingCache guavaCache = guavaCacheMap.get(cacheName);
        if (guavaCache == null) {
            return 0;
        }
        guavaCache.invalidate(key);
        return 1;
    }

    private LoadingCache getGuavaCache(long expireTime, TimeUnit timeUnit) {
        String cacheName = getCacheName(expireTime, timeUnit);
        LoadingCache guavaCache = guavaCacheMap.get(cacheName);
        if (guavaCache == null) {
            guavaCache = newGuavaCache(expireTime, timeUnit);
            guavaCacheMap.put(cacheName, guavaCache);
        }
        return guavaCache;
    }

    private LoadingCache newGuavaCache(long expireTime, TimeUnit timeUnit) {
        if (!isGroupByExpireTime()) {
            return defaultGuavaCache();
        }
        return newOneGuavaCache(expireTime, timeUnit);
    }

    private LoadingCache newOneGuavaCache(long expireTime, TimeUnit timeUnit) {
        return CacheBuilder.newBuilder()
                // 设置初始缓存大小
                .initialCapacity(getInitCapacity())
                // 设置最大缓存
                .maximumSize(getMaxCapacity())
                // 设置缓存线程池
                //.executor(null)
                // 设置定时任务执行过期清除操作
                //.scheduler(Scheduler.systemScheduler())
                // 监听器(超出最大缓存)
                .removalListener(new GuavaCacheRemoveListener())
                // 设置缓存写入后的过期时间
                .expireAfterWrite(expireTime, timeUnit)
                // 开启metrics监控
                .recordStats()
                .build(new CacheLoader() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return null;
                    }
                });
    }

    private LoadingCache defaultGuavaCache() {
        return newOneGuavaCache(getDefaultExpireTime(), getDefaultTimeUnit());
    }
}
