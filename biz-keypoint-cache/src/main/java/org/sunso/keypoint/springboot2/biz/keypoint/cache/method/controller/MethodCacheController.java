package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.controller;

import org.springframework.web.bind.annotation.*;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/method/cache/")
public class MethodCacheController {

    @GetMapping("/remove/distribute/{distributeCacheType}/{keyPattern}")
    public int removeDistributePatternKey(@PathVariable String distributeCacheType, @PathVariable String keyPattern) throws Exception {
        return getDistributeCacheByCacheType(distributeCacheType).removeByPatternKey(keyPattern);
    }

    @GetMapping("/remove/{distributeCacheType}/{LocalCacheType}/{groupByExpireTime}/{key}")
    public int removeKey(@PathVariable String distributeCacheType, @PathVariable String LocalCacheType, @PathVariable boolean groupByExpireTime, @PathVariable String key) throws Exception {
        getDistributeCacheByCacheType(distributeCacheType).remove(key);
        return getLocalCacheByCacheType(LocalCacheType, groupByExpireTime).remove(key);
    }

    @GetMapping("/keys/local/{cacheType}/{groupByExpireTime}")
    public Map<String, Set<Object>> keys(@PathVariable String cacheType, @PathVariable boolean groupByExpireTime) throws Exception {
        return getLocalCacheByCacheType(cacheType, groupByExpireTime).keys();
    }

    @PostMapping("/clear/local/{cacheType}/{groupByExpireTime}")
    public int clear(@PathVariable String cacheType, @PathVariable boolean groupByExpireTime) throws Exception {
       return getLocalCacheByCacheType(cacheType, groupByExpireTime).clear();
    }

    private DistributeCache getDistributeCacheByCacheType(String distributeCacheType) throws Exception {
        DistributeCacheTypeEnum distributeCacheTypeEnum = DistributeCacheTypeEnum.getByCacheType(distributeCacheType);
        if (distributeCacheTypeEnum == null) {
            throw new Exception(String.format("distribute cacheType[%s] not exist", distributeCacheType));
        }
        DistributeCache distributeCache = DistributeCacheManager.getCache(distributeCacheTypeEnum);
        if (distributeCache == null) {
            throw new Exception(String.format("get DistributeCache is null by cacheType[%s]", distributeCacheType));
        }
        return distributeCache;
    }

    private LocalCache getLocalCacheByCacheType(String cacheType, boolean groupByExpireTime) throws Exception {
        LocalCacheTypeEnum localCacheTypeEnum = LocalCacheTypeEnum.getByCacheType(cacheType);
        if (localCacheTypeEnum == null) {
            throw new Exception(String.format("local cacheType[%s] not exist", cacheType));
        }
        LocalCache localCache = LocalCacheManager.getCache(localCacheTypeEnum, LocalCacheModel.newInstance(groupByExpireTime));
        if (localCache == null) {
            throw new Exception(String.format("get LocalCache is null by cacheType[%s]", cacheType));
        }
        return localCache;
    }
}
