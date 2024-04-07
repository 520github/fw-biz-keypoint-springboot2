package org.sunso.keypoint.springboot2.common.scanner;

import java.lang.reflect.Method;

/**
 * @author sunso520
 * @Title:ScannerAnnotationHandler
 * @Description: <br>
 * @Created on 2024/4/7 15:56
 */
public interface ScannerAnnotationHandler {

    boolean classContainAnnotation(Class<?> clazz);

    boolean methodContainAnnotation(Class<?> clazz, Method method);

    void doWithContainAnnotation(Class<?> clazz, Method method);
}
