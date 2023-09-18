package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.exception.DistributeLockException;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.executer.DistributeLockExecuter;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model.DistributeLockModel;

import java.util.function.Function;

public interface DistributeLockHandler {

    Object handle(DistributeLockModel model, DistributeLockExecuter executer);

    Object handleWithException(DistributeLockModel model, DistributeLockExecuter executer) throws DistributeLockException;

    <T, R> R handle(DistributeLockModel model, Function<T, R> function, T functionPara);

    <T, R> R handleWithException(DistributeLockModel model, Function<T, R> function, T functionPara)  throws DistributeLockException;
}
