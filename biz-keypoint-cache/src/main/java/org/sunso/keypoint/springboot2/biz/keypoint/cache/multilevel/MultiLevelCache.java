package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel;

import java.util.concurrent.TimeUnit;

public interface MultiLevelCache {

    Object get(String key);
    Object get(String key, long expireTime, TimeUnit timeUnit);

    void set(String key, Object value, long expireTime, TimeUnit timeUnit);

    int remove(String key);

    int remove(String key, long expireTime, TimeUnit timeUnit);

    int clear();
}
