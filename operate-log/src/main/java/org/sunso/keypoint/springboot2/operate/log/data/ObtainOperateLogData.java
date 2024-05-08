package org.sunso.keypoint.springboot2.operate.log.data;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author sunso520
 * @Title:ObtainOperateLogData
 * @Description: <br>
 * @Created on 2024/5/8 10:05
 */
public interface ObtainOperateLogData {

    void obtainDataBeforeExecuteMethod(OperateLogData operateLogData, ProceedingJoinPoint joinPoint);


    void obtainDataAfterExecuteMethod(OperateLogData operateLogData, Object result, Throwable exception);

}
