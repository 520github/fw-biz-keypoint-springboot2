package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model;

import lombok.Data;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.enums.LockTypeEnum;

import java.util.concurrent.TimeUnit;


@Data
public class DistributeLockModel {

    /**
     * 锁类型
     */
    private LockTypeEnum lockTypeEnum = LockTypeEnum.REENTRANT;
    /**
     * 锁key
     */
    private String lockKey;
    /**
     * 锁等待时间
     */
    private long waitTime = -1;
    /**
     * 锁过期时间
     */
    private long expireTime = 5*60;
    /**
     * 时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public static DistributeLockModel newInstance(String lockKey) {
        DistributeLockModel instance = new DistributeLockModel();
        instance.setLockKey(lockKey);
        return instance;
    }

}
