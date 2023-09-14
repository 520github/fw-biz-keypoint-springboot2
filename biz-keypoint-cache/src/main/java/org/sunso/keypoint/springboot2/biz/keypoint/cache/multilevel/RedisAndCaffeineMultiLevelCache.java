package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel;

import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.config.MultiLevelCacheConfig;

public class RedisAndCaffeineMultiLevelCache extends CustomMultiLevelCache {
    public RedisAndCaffeineMultiLevelCache(MultiLevelCacheConfig multiLevelCacheConfig) {
        super(DistributeCacheManager.getCache(DistributeCacheTypeEnum.redis),
                LocalCacheManager.getCache(LocalCacheTypeEnum.caffeine, LocalCacheModel.newInstance(multiLevelCacheConfig)),
                multiLevelCacheConfig);
    }
}
