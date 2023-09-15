package org.sunso.keypoint.springboot2.biz.keypoint.cache.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CacheMessage;

@Slf4j
public class LocalCacheRemoveNotify {
    private RedisTemplate<String, Object> redisTemplate;

    public LocalCacheRemoveNotify(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void notifyLocalCacheRemove(CacheMessage message) {
        redisTemplate.convertAndSend(message.getChannel(), message);
        log.info("notifyLocalCacheRemove message[{}] to channel[{}]", message, message.getChannel());
    }
}
