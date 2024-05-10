package org.sunso.keypoint.springboot2.operate.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.sunso.keypoint.springboot2.operate.log.annotation.OperateLog;
import org.sunso.keypoint.springboot2.operate.log.context.OperateLogContext;
import org.sunso.keypoint.springboot2.operate.log.data.ObtainOperateLogData;
import org.sunso.keypoint.springboot2.operate.log.data.OperateLogData;

/**
 * @author sunso520
 * @Title:OperateLogAspect
 * @Description: <br>
 * @Created on 2024/5/8 09:21
 */
@Aspect
@Slf4j
public class OperateLogAspect {
    @Autowired
    private ObtainOperateLogData obtainOperateLogData;

    @Around("@annotation(operateLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {
        OperateLogData operateLogData = OperateLogData.newInstance();
        try {
            obtainOperateLogData.obtainDataBeforeExecuteMethod(operateLogData, operateLog, joinPoint );
            Object result =  joinPoint.proceed();
            obtainOperateLogData.obtainDataAfterExecuteMethod(operateLogData, operateLog, result, null);
            return result;
        } catch (Throwable e) {
            obtainOperateLogData.obtainDataAfterExecuteMethod(operateLogData, operateLog, null, e);
            throw e;
        } finally {
            OperateLogContext.removeEXTS();
        }
    }
}
