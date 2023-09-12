package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;


import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class DistributeRedisCache implements DistributeCache {

    private RedisTemplate<Object, Object> redisTemplate;

    public DistributeRedisCache() {

    }

    public DistributeRedisCache(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(Object key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }
}
