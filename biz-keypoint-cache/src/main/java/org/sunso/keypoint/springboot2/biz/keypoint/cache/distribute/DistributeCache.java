package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;

import java.util.concurrent.TimeUnit;

public interface DistributeCache {
    Object get(Object key);

    void set(Object key, Object value, long expireTime, TimeUnit timeUnit);
}
