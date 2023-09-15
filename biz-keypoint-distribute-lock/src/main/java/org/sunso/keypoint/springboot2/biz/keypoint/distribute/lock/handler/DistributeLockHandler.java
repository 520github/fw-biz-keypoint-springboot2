package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.executer.DistributeLockExecuter;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model.DistributeLockModel;

import java.util.function.Function;

public interface DistributeLockHandler {

    Object handle(DistributeLockModel model, DistributeLockExecuter executer);

    <T, R> R handle(DistributeLockModel model, Function<T, R> function, T functionPara);
}
