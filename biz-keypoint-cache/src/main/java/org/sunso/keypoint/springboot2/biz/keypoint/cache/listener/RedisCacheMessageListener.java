package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Data
public class RedisCacheMessageListener extends AbstractChannelMessageListener<CacheMessage> {

    private CaffeineCache caffeineCache;

    @Override
    public void onMessage(CacheMessage message) {
        printCaffeineCache();
        log.info("RedisCacheMessageListener listener message[{}]", message);
        if (Objects.isNull(message.getKey())) {
            caffeineCache.clear();
        }
        else {
            caffeineCache.evict(message.getKey());
        }
    }

    private void printCaffeineCache() {
        Cache<Object,Object> cache = caffeineCache.getNativeCache();
        log.info("RedisCacheMessageListener printCaffeineCache cache size [{}]", cache.asMap().size());
        Map<Object, Object> map = cache.asMap();
        for(Object key: map.keySet()) {
            Object value = map.get(key);
            log.info("RedisCacheMessageListener printCaffeineCache key[{}], value[{}]", key, value);
        }
    }
}
