package org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute;

import org.springframework.data.redis.core.RedisTemplate;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.spring.SpringEnvironment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DistributeCacheManager {

    private static Map<DistributeCacheTypeEnum, DistributeCache> distributeCacheMap = new ConcurrentHashMap<>();

    private static DistributeCacheManager INSTANCE = new DistributeCacheManager();

    private DistributeCacheManager() {

    }

    public DistributeCacheManager getInstance() {
        return INSTANCE;
    }

    public static DistributeCache getCache(DistributeCacheTypeEnum distributeCacheTypeEnum) {
        DistributeCache distributeCache = distributeCacheMap.get(distributeCacheTypeEnum);
        if (distributeCache == null) {
            distributeCache = newDistributeCache(distributeCacheTypeEnum);
        }
        return distributeCache;
    }

    private static DistributeCache newDistributeCache(DistributeCacheTypeEnum distributeCacheTypeEnum) {
        if (DistributeCacheTypeEnum.redis == distributeCacheTypeEnum) {
            return new DistributeRedisCache(SpringEnvironment.getBean("redisTemplate", RedisTemplate.class));
        }
        return null;
    }
}
