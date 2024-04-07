package org.sunso.keypoint.springboot2.common.scanner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunso520
 * @Title:Scanner
 * @Description: <br>
 * @Created on 2024/4/7 16:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scanner {
    String key();

    String name();

    int parentId();
}
