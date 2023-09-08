package org.sunso.keypoint.springboot2.controller.intercept.global.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.sunso.keypoint.springboot2.spring.SpringEnvironment;
import org.sunso.keypoint.springboot2.spring.log.BaseLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

//import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;

public abstract class AbstractControllerLog extends BaseLog {

    protected Object doProceed(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = getRequestUrl(request);
        //不打印拦截日志
        if (isRequestLogClose() || SpringEnvironment.getLogConfig().getExcludeRequestUrl().contains(url)) {
            return joinPoint.proceed();
        }
        String requestParams = getRequestParams(joinPoint);
        long timestamp = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        printLog(url, requestParams, result, timestamp);
        return result;
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private void printLog(String url, String requestParams, Object result, long startTime) {
        try {
            if (requestParams != null && requestParams.length() > closeRequestLogLength()) {
                requestParams = "request length " + requestParams.length() + " > closeRequestLogLength " + closeRequestLogLength();
            }
            if (closeRequestLogLength() >0) {
                logInfo("接口请求地址[{}],请求参数[{}]", url, requestParams);
            }
            String response = getResultString(result);
            if (response != null && response.length() > closeResponseLogLength()) {
                response = "response length " + response.length() + " > closeResponseLogLength " + closeResponseLogLength();
            }
            if (closeResponseLogLength() >0) {
                logInfo("接口请求地址[{}],接口耗时[{}毫秒],响应结果[{}]", url, (System.currentTimeMillis()-startTime), response);
            }
        }catch (Exception e) {
            logError("ControllerLogAspect打印日志发生异常", e);
        }
    }

    private String getResultString(Object result) {
        try {
            if (result == null) {
                return null;
            }
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logError("获取响应结果发生异常", e);
            return "获取响应结果发生异常"+e.getMessage();
        }
    }

    /**
     * 获取方法参数带RequestBody注解的内容
     *
     * @param pjp
     * @return
     */
    private String getRequestParams(ProceedingJoinPoint pjp) {
        try {
            String parameterStr = "";
            Object[] parameterValues = pjp.getArgs();
            Parameter[] parameter = getParameterName(pjp);
            for (int i = 0; i < parameter.length; i++) {
                Parameter para = parameter[i];
                if (para.getType().isAssignableFrom(HttpServletRequest.class) ) {
                    continue;
                }
                if (para.getType().isAssignableFrom(HttpServletResponse.class)) {
                    continue;
                }
                if (para.getType().isAssignableFrom(MultipartFile.class)) {
                    continue;
                }
                String value = null;
                if (parameterValues[i] != null) {
                    value = JSON.toJSONString(parameterValues[i]);
                }
                parameterStr += para.getName()+"="+value+ ",";
            }
            return parameterStr;
        }catch (Exception e) {
            logError("获取请求参数发生异常", e);
            return "获取请求参数发生异常"+e.getMessage();
        }
    }

    private Parameter[] getParameterName(ProceedingJoinPoint pjp) {
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method method = ms.getMethod();
        return method.getParameters();
    }
}
