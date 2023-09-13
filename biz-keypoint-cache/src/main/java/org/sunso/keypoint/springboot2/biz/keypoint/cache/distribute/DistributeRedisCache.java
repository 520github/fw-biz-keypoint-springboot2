package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DistributeRedisCache implements DistributeCache {

    private RedisTemplate<String,Object> redisTemplate;

    public DistributeRedisCache() {

    }

    public DistributeRedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        log.info("DistributeRedisCache getKeySerializer[{}]", redisTemplate.getKeySerializer().getClass().getName());
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    @Override
    public int remove(String key) {
        redisTemplate.delete(key);
        return 1;
    }
}
