package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.exception;

public class DistributeLockException extends RuntimeException{
    public DistributeLockException(String msg) {
        super(msg);
    }

    public DistributeLockException(String msg, Throwable t) {
        super(msg, t);
    }
}
