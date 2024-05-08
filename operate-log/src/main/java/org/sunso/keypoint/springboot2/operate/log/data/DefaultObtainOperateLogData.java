package org.sunso.keypoint.springboot2.operate.log.data;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.sunso.keypoint.springboot2.spring.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author sunso520
 * @Title:DefaultObtainOperateLogData
 * @Description: <br>
 * @Created on 2024/5/8 10:22
 */
public class DefaultObtainOperateLogData implements ObtainOperateLogData {

    @Override
    public void obtainDataBeforeExecuteMethod(OperateLogData operateLogData, ProceedingJoinPoint joinPoint) {
        operateLogData.setStartTime(LocalDateTime.now());
        obtainUserData(operateLogData);
        obtainRequestData(operateLogData);
        obtainMethodData(operateLogData, joinPoint);
        obtainBeforeData(operateLogData);
    }

    private void obtainUserData(OperateLogData operateLogData) {
        operateLogData.setUserId("");
        operateLogData.setUserName("");
        operateLogData.setUserType("");
        operateLogData.setUserRoles("");
    }

    private void obtainBeforeData(OperateLogData operateLogData) {
        operateLogData.setBeforeData("");
    }

    private void obtainMethodData(OperateLogData operateLogData, ProceedingJoinPoint joinPoint) {
        operateLogData.setJavaMethodName(((MethodSignature) joinPoint.getSignature()).toString());
        operateLogData.setJavaMethodArgs(obtainMethodArgs(joinPoint));
    }

    private void obtainRequestData(OperateLogData operateLogData) {
        operateLogData.setRequestUrl(ServletUtils.getRequestUrl());
        operateLogData.setRequestMethod(ServletUtils.getRequestMethod());
        operateLogData.setUserIp(ServletUtils.getClientIP());
        operateLogData.setUserAgent(ServletUtils.getUserAgent());
        operateLogData.setClientType(ServletUtils.getHeaderValueByKey("Client-Type"));
        operateLogData.setClientVersion(ServletUtils.getHeaderValueByKey("Client-Version"));
    }

    private static String obtainMethodArgs(ProceedingJoinPoint joinPoint) {
        // TODO 提升：参数脱敏和忽略
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        // 拼接参数
        Map<String, Object> args = new HashMap<>();
                //Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // 被忽略时，标记为 ignore 字符串，避免和 null 混在一起
            args.put(argName, !isIgnoreArgs(argValue) ? argValue : "[ignore]");
        }
        //return JsonUtils.toJsonString(args);
        return JSONUtil.toJsonStr(args);
    }

    private static boolean isIgnoreArgs(Object object) {
        Class<?> clazz = object.getClass();
        // 处理数组的情况
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object))
                    .anyMatch(index -> isIgnoreArgs(Array.get(object, index)));
        }
        // 递归，处理数组、Collection、Map 的情况
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream()
                    .anyMatch((Predicate<Object>) DefaultObtainOperateLogData::isIgnoreArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreArgs(((Map<?, ?>) object).values());
        }
        // obj
        return object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult;
    }

    @Override
    public void obtainDataAfterExecuteMethod(OperateLogData operateLogData, Object result, Throwable exception) {
        operateLogData.setDuration((LocalDateTimeUtil.between(operateLogData.getStartTime(), LocalDateTime.now()).toMillis()));
        if (result != null) {
            JSONObject jsonData = JSONUtil.parseObj(JSONUtil.toJsonStr(result));
            operateLogData.setResultCode(getCode(jsonData));
            operateLogData.setResultMsg(getMsg(jsonData));
            operateLogData.setResultData(getData(jsonData));
        }
        if (exception != null) {
            operateLogData.setResultCode("500");
            operateLogData.setResultMsg(ExceptionUtil.getRootCauseMessage(exception));
        }
        obtainAfterData(operateLogData);
    }

    private void obtainAfterData(OperateLogData operateLogData) {
        operateLogData.setAfterData("");
    }

    private String getCode(JSONObject jsonData) {
        if (jsonData == null) {
            return null;
        }
        return jsonData.getStr("code");
    }

    private String getMsg(JSONObject jsonData) {
        if (jsonData == null) {
            return null;
        }
        return jsonData.getStr("msg");
    }

    private String getData(JSONObject jsonData) {
        if (jsonData == null) {
            return null;
        }
        Object data =  jsonData.getObj("data");
        if (data == null) {
            return null;
        }
        return JSONUtil.toJsonStr(data);
    }

}
