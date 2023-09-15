package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.concurrent.TimeUnit;

public abstract class AbstractLocalCache implements LocalCache {

    protected LocalCacheModel localCacheModel;

    protected AbstractLocalCache(LocalCacheModel localCacheModel) {
        this.localCacheModel = localCacheModel;
    }

    protected String getCacheName(long expireTime, TimeUnit timeUnit) {
        if (!isGroupByExpireTime()) {
            return getPreCacheName();
        }
        return new StringBuffer()
                .append(getPreCacheName())
                .append("-").append(expireTime).append("-")
                .append(timeUnit.name()).toString();
    }

    private String getPreCacheName() {
        return "local-cache-" + cacheType();
    }

    protected Long getDefaultExpireTime() {
        return 5*60L;
    }

    protected TimeUnit getDefaultTimeUnit() {
        return TimeUnit.SECONDS;
    }

    protected int getInitCapacity() {
        return localCacheModel.getInitCapacity();
    }

    protected int getMaxCapacity() {
        return localCacheModel.getMaxCapacity();
    }

    protected boolean isGroupByExpireTime() {
        return localCacheModel.isGroupByExpireTime();
    }
}
