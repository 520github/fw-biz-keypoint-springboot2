package org.sunso.keypoint.springboot2.biz.keypoint.cache.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.aspect.MethodCacheAspect;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.config.MethodCacheConfig;

@Configuration
@EnableConfigurationProperties(MethodCacheConfig.class)
public class MethodCacheAutoConfiguration {

    @Bean
    public MethodCacheAspect methodCacheAspect() {
        return new MethodCacheAspect();
    }
}
