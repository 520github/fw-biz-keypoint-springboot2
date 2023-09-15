package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.enums.LockTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.executer.DistributeLockExecuter;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model.DistributeLockModel;

import java.util.function.Function;

@Slf4j
public class RedissonDistributeLockHandler implements DistributeLockHandler {

    private RedissonClient redissonClient;

    public RedissonDistributeLockHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Object handle(DistributeLockModel model, DistributeLockExecuter executer) {
        RLock rLock = null;
        try {
            rLock = getLock(model.getLockTypeEnum(), model.getLockKey());
            if (model.getWaitTime() > 0) {
            }
            if (rLock.tryLock(model.getWaitTime(), model.getLeaseTime(), model.getTimeUnit())) {
                log.info("获取分布式锁key[{}]成功", model.getLockKey());
                return executer.execute();
            }
            log.info("获取分布式锁失败[{}]" + model);
        }catch (Exception e) {
            log.error("获取分布式锁发生异常", e);
        }finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                log.info("释放分布式锁key[{}]成功", model.getLockKey());
                rLock.unlock();
            }
        }
        return null;
    }

    @Override
    public <T, R> R handle(DistributeLockModel model, Function<T, R> function, T functionPara) {
        return (R)handle(model, () -> function.apply(functionPara));
    }


    RLock getLock(LockTypeEnum lockType, String lockKey) {
        // 获取一把锁，只要锁的名字一样，就是同一把锁
        RLock rLock ;
        switch (lockType) {
            case FAIR:
                rLock = redissonClient.getFairLock(lockKey);
                break;
            case REENTRANT:
                rLock = redissonClient.getLock(lockKey);
                break;
            case READ:
                RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockKey);
                rLock = readWriteLock.readLock();
                break;
            case WRITE:
                RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockKey);
                rLock = rwLock.writeLock();
                break;
            default:
                rLock = redissonClient.getLock(lockKey);
        }
        return rLock;

    }
}
