package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import java.util.concurrent.TimeUnit;

public interface LocalCache {
    Object get(String key, long expireTime, TimeUnit timeUnit);

    void set(String key, Object value, long expireTime, TimeUnit timeUnit);

    int remove(String key);

    int remove(String key, long expireTime, TimeUnit timeUnit);
}
