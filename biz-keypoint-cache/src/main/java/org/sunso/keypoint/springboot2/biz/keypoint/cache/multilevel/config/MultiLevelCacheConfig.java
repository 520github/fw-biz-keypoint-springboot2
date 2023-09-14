package org.sunso.keypoint.springboot2.biz.keypoint.cache.multilevel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;

@ConfigurationProperties(prefix = "cache.multilevel")
@Data
public class MultiLevelCacheConfig {
    /**
     * 多级缓存名称
     */
    private String cacheName = "cache_multilevel";

    /**
     * 缓存默认过期时间(24小时)
     */
    private Integer cacheDefaultExpireTime = 24*60*60;

    /**
     * 一级缓存开关
     */
    private boolean firstLevelSwitch = true;

    /**
     * 一级缓存是否按过期时间进行分组
     */
    private boolean firstLevelGroupByExpireTime = true;

    /**
     * 一级本地缓存最大容量
     */
    private Integer firstLevelMaxCapacity = 512;

    /**
     * 一级本地缓存初始化容量
     */
    private Integer firstLevelInitCapacity = 64;

    /**
     * 一级缓存类型
     */
    private String firstLevelCacheType = LocalCacheTypeEnum.caffeine.getType();

    /**
     * 二级缓存类型
     */
    private String secondFirstLevelCacheType = DistributeCacheTypeEnum.redis.getType();


    /**
     * 二级缓存名称
     */
    //private String firstLevelCacheName = "cache-multilevel-caffeine";

    /**
     * 二级缓存过期时间
     */
    //private Integer secondLevelCacheExpireTime = 600;

    /**
     * 二级缓存名称
     */
    //private String secondLevelCacheName = "cache-multilevel-redis";
}
