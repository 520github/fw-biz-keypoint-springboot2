package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.AbstractChannelMessageListener;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.listener.CacheMessage;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Data
public class MultiLevelCacheMessageListener extends AbstractChannelMessageListener<CacheMessage> {

    @Override
    public void onMessage(CacheMessage message) {
        LocalCache localCache = getLocalCache(message);
        if (localCache == null) {
            log.info("MultiLevelCacheMessageListener listener get localCache is null by CacheMessage[{}]", message);
            return;
        }
        printLocalCache(localCache);
        String key = message.getKey();
        log.info("MultiLevelCacheMessageListener listener message[{}]", message);
        if (Objects.isNull(key)) {
            localCache.clear();
        }
        else if (message.getExpireTime() == null) {
            localCache.remove(key);
        }
        else {
            localCache.remove(key, message.getExpireTime(), message.getTimeUnit());
        }
    }

    private LocalCache getLocalCache(CacheMessage message) {
        LocalCacheTypeEnum localCacheTypeEnum = LocalCacheTypeEnum.getByCacheType(message.getLocalCacheType());
        if (localCacheTypeEnum == null) {
            log.info("MultiLevelCacheMessageListener listener get LocalCacheTypeEnum is null by localCacheType[{}]", message.getLocalCacheType());
            return null;
        }
        return LocalCacheManager.getCache(localCacheTypeEnum, LocalCacheModel.newInstance(message.isLocalCacheGroupByExpireTime()));
    }

    private void printLocalCache(LocalCache localCache) {
        Map<String, Set<Object>> keysMap = localCache.keys();
        log.info("MultiLevelCacheMessageListener printLocalCache cache size [{}]", keysMap.size());
        for(Object key: keysMap.keySet()) {
            Object value = keysMap.get(key);
            log.info("MultiLevelCacheMessageListener printLocalCache key[{}], value[{}]", key, value);
        }
    }
}
