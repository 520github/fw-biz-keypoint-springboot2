package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.sunso.keypoint.springboot2.spring.redis.RedisOperate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DistributeRedisCache implements DistributeCache {

    private RedisTemplate<String,Object> redisTemplate;
    private RedisOperate redisOperate;

    public DistributeRedisCache() {

    }

    public DistributeRedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisOperate = new RedisOperate(redisTemplate);
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
        log.info("DistributeRedisCache remove key[{}]", key);
        return 1;
    }

    @Override
    public int removeByPatternKey(String patternKey) {
        return redisOperate.delByKeyPattern(patternKey);
    }
}
