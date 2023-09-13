package org.sunso.keypoint.springboot2.biz.keypoint.cache.model;

import lombok.Data;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.config.MethodCacheConfig;

@Data
public class LocalCacheModel {
    /**
     * 是否按过期时间进行分组
     */
    private boolean groupByExpireTime = false;
    /**
     * 初始化容量
     */
    private Integer initCapacity = 1000;
    /**
     * 最大容量
     */
    private Integer maxCapacity = 10000;


    public static LocalCacheModel newInstance(MethodCacheConfig methodCacheConfig) {
        LocalCacheModel instance = new LocalCacheModel();
        instance.setInitCapacity(methodCacheConfig.getLocalCacheInitCapacity());
        instance.setMaxCapacity(methodCacheConfig.getLocalCacheMaxCapacity());
        instance.setGroupByExpireTime(methodCacheConfig.isLocalCacheGroupByExpireTime());
        return instance;
    }
}
