package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface LocalCache {
    Object get(String key, long expireTime, TimeUnit timeUnit);

    <T> T get(String key, long expireTime, TimeUnit timeUnit, Class<T> tClass);

    void set(String key, Object value, long expireTime, TimeUnit timeUnit);

    int remove(String key);

    int remove(String key, long expireTime, TimeUnit timeUnit);

    Map<String, Set<Object>> keys();

    int clear();

    String cacheType();
}
