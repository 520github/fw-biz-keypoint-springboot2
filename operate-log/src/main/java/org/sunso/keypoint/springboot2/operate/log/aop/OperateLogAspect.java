package org.sunso.keypoint.springboot2.operate.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.sunso.keypoint.springboot2.operate.log.annotation.OperateLog;

/**
 * @author sunso520
 * @Title:OperateLogAspect
 * @Description: <br>
 * @Created on 2024/5/8 09:21
 */
@Aspect
@Slf4j
public class OperateLogAspect {

    @Around("@annotation(operateLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperateLog operateLog) {
        return null;
    }
}
