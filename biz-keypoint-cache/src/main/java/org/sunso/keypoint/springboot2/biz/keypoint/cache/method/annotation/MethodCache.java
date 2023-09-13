package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.annotation;

import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodCache {

    /**
     * 过期时间，默认5分钟
     * @return
     */
    int expireTime() default 5*60;

    /**
     * 过期单位默认为秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 业务前缀键
     * @return
     */
    String bizPrefixKey() default "default";

    /**
     * 本地缓存类型
     * @return
     */
    LocalCacheTypeEnum localCacheType() default LocalCacheTypeEnum.caffeine;

    /**
     * 分布式缓存类型
     * @return
     */
    DistributeCacheTypeEnum distributeCacheType() default DistributeCacheTypeEnum.redis;
}
