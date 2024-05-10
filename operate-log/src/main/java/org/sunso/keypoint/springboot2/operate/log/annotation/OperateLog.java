package org.sunso.keypoint.springboot2.operate.log.annotation;

import org.sunso.keypoint.springboot2.operate.log.enums.DefaultOperateBizEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunso520
 * @Title:OperateLog
 * @Description: <br>
 * @Created on 2024/5/8 09:25
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    /**
     * 对应操作模块名称
     * @return
     */
    String moduleName() default "";

    /**
     * 对应操作功能名称
     * @return
     */
    String functionName() default "";

    /**
     * 是否记录方法的参数
     * @return
     */
    boolean logMethodArgs() default true;

    /**
     * 是否记录返回结果数据
     * @return
     */
    boolean logResultData() default true;


    DefaultOperateBizEnum operateBizEnum() default DefaultOperateBizEnum.EMPTY;
}
