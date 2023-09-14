package org.sunso.keypoint.springboot2.biz.keypoint.cache.model;

import lombok.Data;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.config.MethodCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.config.MultiLevelCacheConfig;

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

    public static LocalCacheModel newInstance(boolean groupByExpireTime) {
        LocalCacheModel instance = new LocalCacheModel();
        instance.setGroupByExpireTime(groupByExpireTime);
        return instance;
    }

    public static LocalCacheModel newInstance(MethodCacheConfig methodCacheConfig) {
        LocalCacheModel instance = new LocalCacheModel();
        instance.setInitCapacity(methodCacheConfig.getLocalCacheInitCapacity());
        instance.setMaxCapacity(methodCacheConfig.getLocalCacheMaxCapacity());
        instance.setGroupByExpireTime(methodCacheConfig.isLocalCacheGroupByExpireTime());
        return instance;
    }

    public static LocalCacheModel newInstance(MultiLevelCacheConfig multiLevelCacheConfig) {
        LocalCacheModel instance = new LocalCacheModel();
        instance.setGroupByExpireTime(multiLevelCacheConfig.isFirstLevelGroupByExpireTime());
        instance.setMaxCapacity(multiLevelCacheConfig.getFirstLevelMaxCapacity());
        instance.setInitCapacity(multiLevelCacheConfig.getFirstLevelInitCapacity());
        return instance;
    }
}
