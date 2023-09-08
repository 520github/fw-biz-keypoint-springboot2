package org.sunso.keypoint.springboot2.controller.intercept.global.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.sunso.keypoint.springboot2.common.model.R;
import org.sunso.keypoint.springboot2.controller.intercept.global.annotation.NeedResponseBodyAdvice;
import org.sunso.keypoint.springboot2.controller.intercept.global.annotation.NotNeedResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ControllerResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), NeedResponseBodyAdvice.class)
        || methodParameter.hasMethodAnnotation(NeedResponseBodyAdvice.class)) {
            return true;
        }
        if (AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), NotNeedResponseBodyAdvice.class)
                || methodParameter.hasMethodAnnotation(NotNeedResponseBodyAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("ControllerResponseBodyAdvice path[{}]", serverHttpRequest.getURI().getPath());
        Method method = returnType.getMethod();
        Class<?> returnClass = method.getReturnType();
        if (Objects.equals(returnClass, String.class)) {
            return o;
        }
        if (o != null && o instanceof R) {
            return o;
        }
        return R.ok(o);
    }
}
