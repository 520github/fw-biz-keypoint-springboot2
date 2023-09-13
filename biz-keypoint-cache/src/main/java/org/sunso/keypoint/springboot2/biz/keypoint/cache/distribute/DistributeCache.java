package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;

import java.util.concurrent.TimeUnit;

public interface DistributeCache {
    Object get(String key);

    void set(String key, Object value, long expireTime, TimeUnit timeUnit);

    int remove(String key);
}
