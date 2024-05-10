package org.sunso.keypoint.springboot2.operate.log.data;

import org.aspectj.lang.ProceedingJoinPoint;
import org.sunso.keypoint.springboot2.operate.log.annotation.OperateLog;
import org.sunso.keypoint.springboot2.operate.log.enums.OperateBiz;

/**
 * @author sunso520
 * @Title:ObtainOperateLogData
 * @Description: <br>
 * @Created on 2024/5/8 10:05
 */
public interface ObtainOperateLogData {

    void obtainDataBeforeExecuteMethod(OperateLogData operateLogData, OperateLog operateLog, ProceedingJoinPoint joinPoint);


    void obtainDataAfterExecuteMethod(OperateLogData operateLogData, OperateLog operateLog, Object result, Throwable exception);

}
