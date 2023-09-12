package org.sunso.keypoint.springboot2.biz.keypoint.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.method")
@Data
public class MethodCacheConfig {

    /**
     * 本地缓存开关
     */
    private boolean localCacheSwitch = true;

}
