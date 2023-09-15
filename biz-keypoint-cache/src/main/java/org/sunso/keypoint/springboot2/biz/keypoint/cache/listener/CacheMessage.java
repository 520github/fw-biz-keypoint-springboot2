package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import lombok.Data;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.constant.Constants;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
public class CacheMessage extends AbstractChannelMessage implements Serializable {

    private String cacheName;
    private String localCacheType;
    private boolean localCacheGroupByExpireTime;
    private String key;
    private Object value;
    private Long expireTime;
    private TimeUnit timeUnit;

    @Override
    public String getChannel() {
        return Constants.MULTI_LEVEL_CACHE_REDIS_CHANNEL;
    }

    public static CacheMessage newInstance(String localCacheType,boolean groupByExpireTime) {
        return newInstance(localCacheType, groupByExpireTime, null);
    }
    public static CacheMessage newInstance(String localCacheType,boolean groupByExpireTime, String key) {
        CacheMessage instance = new CacheMessage();
        instance.setLocalCacheType(localCacheType);
        instance.setLocalCacheGroupByExpireTime(groupByExpireTime);
        instance.setKey(key);
        instance.setValue(null);
        instance.setExpireTime(null);
        instance.setTimeUnit(null);
        return instance;
    }


}
