package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.method")
@Data
public class MethodCacheConfig {

    /**
     * 方法缓存的前缀key
     */
    private String MethodCachePrefixKey = "cache_method_";

    /**
     * 本地缓存开关
     */
    private boolean localCacheSwitch = true;

    /**
     * 本地缓存是否按过期时间进行分组
     */
    private boolean localCacheGroupByExpireTime = true;
    /**
     * 本地缓存初始化容量
     */
    private Integer localCacheInitCapacity = 1000;
    /**
     * 本地缓存最大容量
     */
    private Integer localCacheMaxCapacity = 10000;

}
