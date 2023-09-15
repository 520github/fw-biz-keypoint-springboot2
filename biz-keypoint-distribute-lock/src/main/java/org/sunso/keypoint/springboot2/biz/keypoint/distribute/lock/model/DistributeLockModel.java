package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model;

import lombok.Data;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.enums.LockTypeEnum;

import java.util.concurrent.TimeUnit;


@Data
public class DistributeLockModel {

    private LockTypeEnum lockTypeEnum = LockTypeEnum.REENTRANT;
    private String lockKey;
    private long waitTime = -1;
    private long leaseTime = 30*60;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public static DistributeLockModel newInstance(String lockKey) {
        DistributeLockModel instance = new DistributeLockModel();
        instance.setLockKey(lockKey);
        return instance;
    }

}
