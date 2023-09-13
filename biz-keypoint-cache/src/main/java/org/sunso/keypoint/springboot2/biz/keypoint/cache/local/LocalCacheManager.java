package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheManager {
    private static Map<LocalCacheTypeEnum, LocalCache> loclCacheMap = new ConcurrentHashMap<>();

    private static LocalCacheManager INSTANCE = new LocalCacheManager();

    private LocalCacheManager() {

    }

    public LocalCacheManager getInstance() {
        return INSTANCE;
    }

    public static LocalCache getCache(LocalCacheTypeEnum localCacheTypeEnum, LocalCacheModel localCacheModel) {
        LocalCache localCache = loclCacheMap.get(localCacheTypeEnum);
        if (localCache == null) {
            localCache = newLocalCache(localCacheTypeEnum, localCacheModel);
            loclCacheMap.put(localCacheTypeEnum, localCache);
        }
        return localCache;
    }

    private static LocalCache newLocalCache(LocalCacheTypeEnum localCacheTypeEnum, LocalCacheModel localCacheModel) {
        if (LocalCacheTypeEnum.caffeine == localCacheTypeEnum) {
            return new LocalCaffeineCache(localCacheModel);
        }
        else if (LocalCacheTypeEnum.guava == localCacheTypeEnum) {
            return new LocalGuavaCache(localCacheModel);
        }
        return null;
    }
}
