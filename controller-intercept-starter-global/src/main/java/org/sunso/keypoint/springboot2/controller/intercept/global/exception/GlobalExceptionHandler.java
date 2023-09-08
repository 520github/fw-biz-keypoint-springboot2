package org.sunso.keypoint.springboot2.controller.intercept.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.sunso.keypoint.springboot2.common.exception.BizRuntimeException;
import org.sunso.keypoint.springboot2.common.model.R;
import org.sunso.keypoint.springboot2.common.status.DefaultResultStatusEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizRuntimeException.class)
    @ResponseBody
    public R bizExceptionHandler(HttpServletRequest request, BizRuntimeException exception) {
        logException(request, exception);
        return R.fail(exception.getResultStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest request, Exception e) {
        logException(request, e);
        //参数检验异常
        if (e instanceof MethodArgumentNotValidException) {
            return R.fail(getMethodArgumentNotValidData((MethodArgumentNotValidException)e));
        }
        //请求方法错误
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            return R.fail("MethodNotSupported", "请求方法错误");
        }
        //请求参数缺失
        else if (e instanceof MissingServletRequestParameterException) {
            return R.fail("MissingServletRequestParameter", "请求参数缺失");
        }
        //请求参数类型错误
        else if (e instanceof MethodArgumentTypeMismatchException) {
            return R.fail("ArgumentTypeMismatch", "请求参数类型错误");
        }
        //请求地址不存在
        else if (e instanceof NoHandlerFoundException) {
            return R.fail("NoHandlerFound", "请求地址不存在");
        }
        return R.fail(DefaultResultStatusEnum.unknown);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public R throwableHandler(HttpServletRequest request, Throwable throwable) {
        logException(request, throwable);
        return R.fail(DefaultResultStatusEnum.unknown);
    }

    private Map<String, String> getMethodArgumentNotValidData(MethodArgumentNotValidException e) {
        Map<String, String> data = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            data.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return data;
    }

    private void logException(HttpServletRequest request, Throwable exception) {
        log.error(String.format("request url[%s] exception", request.getRequestURI()), exception);
    }
}
