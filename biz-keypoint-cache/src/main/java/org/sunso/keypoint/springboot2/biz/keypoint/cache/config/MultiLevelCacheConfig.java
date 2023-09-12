package org.sunso.keypoint.springboot2.biz.keypoint.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.multilevel")
@Data
public class MultiLevelCacheConfig {
    /**
     * 多级缓存名称
     */
    private String cacheName = "cache_multilevel";

    /**
     * 一级缓存开关
     */
    private boolean firstLevelSwitch = true;

    /**
     * 一级本地缓存最大容量
     */
    private Integer firstLevelMaxCapacity = 512;

    /**
     * 一级本地缓存初始化容量
     */
    private Integer firstLevelInitCapacity = 64;

    /**
     * 一级本地缓存过期时间
     */
    private Integer firstLevelCacheExpireTime = 300;



    /**
     * 二级缓存名称
     */
    private String firstLevelCacheName = "cache-multilevel-caffeine";

    /**
     * 二级缓存过期时间
     */
    private Integer secondLevelCacheExpireTime = 600;

    /**
     * 二级缓存名称
     */
    private String secondLevelCacheName = "cache-multilevel-redis";
}
