package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.annotation;

import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.enums.LockTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributeLock {

    /**
     * 分布式锁key
     * @return
     */
    String lockKey();

    /**
     * 锁类型，默认可重入锁
     * @return
     */
    LockTypeEnum lockType() default LockTypeEnum.REENTRANT;

    /**
     * 分布式锁的等待时间
     * @return
     */
    long waitTime() default 30;

    /**
     * 分布式锁的过期时间
     * @return
     */
    long expireTime() default 5*60;

    /**
     * 时间单位，默认秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
