package org.sunso.keypoint.springboot2.controller.intercept.global.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface NotNeedResponseBodyAdvice {

}
