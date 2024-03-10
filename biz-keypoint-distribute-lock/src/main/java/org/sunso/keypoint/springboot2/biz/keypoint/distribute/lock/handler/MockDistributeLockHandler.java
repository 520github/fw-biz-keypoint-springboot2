package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.exception.DistributeLockException;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.executer.DistributeLockExecuter;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model.DistributeLockModel;

import java.util.function.Function;

/**
 * @author sunso520
 * @Title:MockDistributeLockHandler
 * @Description: <br>
 * @Created on 2024/3/5 11:08
 */
public class MockDistributeLockHandler implements DistributeLockHandler{
    @Override
    public Object handle(DistributeLockModel model, DistributeLockExecuter executer) {
        return null;
    }

    @Override
    public Object handleWithException(DistributeLockModel model, DistributeLockExecuter executer) throws DistributeLockException {
        return null;
    }

    @Override
    public <T, R> R handle(DistributeLockModel model, Function<T, R> function, T functionPara) {
        return function.apply(functionPara);
    }

    @Override
    public <T, R> R handleWithException(DistributeLockModel model, Function<T, R> function, T functionPara) throws DistributeLockException {
        return null;
    }
}
