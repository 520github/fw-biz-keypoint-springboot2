package org.sunso.keypoint.springboot2.controller.intercept.global.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sunso.keypoint.springboot2.controller.intercept.global.aspect.ControllerLogAspect;
import org.sunso.keypoint.springboot2.controller.intercept.global.exception.GlobalExceptionHandler;
import org.sunso.keypoint.springboot2.controller.intercept.global.response.ControllerResponseBodyAdvice;

@Configuration
public class ControllerInterceptGlobalAutoConfiguration {

    @Bean
    public ControllerResponseBodyAdvice controllerResponseBodyAdvice() {
        return new ControllerResponseBodyAdvice();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public ControllerLogAspect controllerLogAspect() {
        return new ControllerLogAspect();
    }


}
