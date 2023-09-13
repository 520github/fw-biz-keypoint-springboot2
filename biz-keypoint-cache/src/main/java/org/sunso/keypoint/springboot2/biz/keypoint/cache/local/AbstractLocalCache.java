package org.sunso.keypoint.springboot2.biz.keypoint.cache.local;

import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;

import java.util.concurrent.TimeUnit;

public abstract class AbstractLocalCache implements LocalCache {

    protected LocalCacheModel localCacheModel;

    protected AbstractLocalCache(LocalCacheModel localCacheModel) {
        this.localCacheModel = localCacheModel;
    }

    protected abstract String cacheType();

    protected String getCacheName(long expireTime, TimeUnit timeUnit) {
        if (!localCacheModel.isGroupByExpireTime()) {
            return "default";
        }
        return new StringBuffer()
                .append("local-cache-")
                .append(cacheType())
                .append("-").append(expireTime).append("-")
                .append(timeUnit.name()).toString();
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
