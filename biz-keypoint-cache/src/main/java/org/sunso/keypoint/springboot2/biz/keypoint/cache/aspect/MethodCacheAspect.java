package org.sunso.keypoint.springboot2.biz.keypoint.cache.aspect;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.annotation.MethodCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.config.MethodCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Slf4j
public class MethodCacheAspect {

    @Resource
    private MethodCacheConfig methodCacheConfig;

    @Pointcut("@annotation(org.sunso.keypoint.springboot2.biz.keypoint.cache.annotation.MethodCache)")
    public void methodCachePoint() {
    }

    @Around(value = "methodCachePoint()")
    public Object doMethodCache(ProceedingJoinPoint point) throws Throwable {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            String className = methodSignature.getClass().getName();

            log.info("MethodCacheAspect doMethodCache className[{}], methodName[{}]", className, method.getName());

            MethodCache methodCache = method.getAnnotation(MethodCache.class);

            String methodCacheKey = getMethodCacheKey(method, point.getArgs(), className);
            if (StrUtil.isBlank(methodCacheKey)) {
                log.error("MethodCacheAspect doMethodCache find methodCacheKey is empty by className[{}], methodName[{}]", className, method.getName());
                return point.proceed();
            }

            //从缓存获取数据
            Object result = getDataFromCache(point, methodCache, methodCacheKey);
            if (result != null) {
                log.error("MethodCacheAspect doMethodCache get data from cache by className[{}], methodName[{}]", className, method.getName());
                return result;
            }

            //执行方法获取数据
            return getDataFromMethod(point, methodCache, methodCacheKey);
        }catch (Exception e) {
            log.error("MethodCacheAspect doMethodCache exception", e);
            return point.proceed();
        }
    }


    /**
     * 从缓存中读取数据
     * @param point
     * @param methodCache
     * @param methodCacheKey
     * @return
     * @throws Throwable
     */
    private Object getDataFromCache(ProceedingJoinPoint point, MethodCache methodCache, String methodCacheKey) throws Throwable {
        //全局本地缓存打开，同时方法支持使用本地缓存， 才从本地缓存读取数据
        if (okLocalCache(methodCache)) {
            return null;
        }
        //分布式缓存为空，直接执行方法
        if (!okDistributeCache(methodCache)) {
            return point.proceed();
        }
        //从分布式缓存读取数据
        Object result = getDistributeCache(methodCache).get(methodCacheKey);
        log.info("getDataFromCache data[{}]", result);
        //设置到本地缓存
        if (result != null && okLocalCache(methodCache)) {

        }
        return result;
    }

    /**
     * 执行方法获取数据
     * @param point
     * @return
     * @throws Throwable
     */
    private Object getDataFromMethod(ProceedingJoinPoint point, MethodCache methodCache, String methodCacheKey) throws Throwable {
        Object result = point.proceed();
        if (result == null) {
            log.info("getDataFromMethod result is null");
            return null;
        }
        if (okDistributeCache(methodCache)) {
            getDistributeCache(methodCache).set(methodCacheKey, result, methodCache.expireTime(), methodCache.timeUnit());
            log.info("getDataFromMethod set result[{}] to distribute cache[{}]", result, methodCache.distributeCacheType());
        }
        if (okLocalCache(methodCache)) {

        }
        return result;
    }

    /**
     *
     * @param methodCache
     * @return
     */
    private boolean okLocalCache(MethodCache methodCache) {
        if (methodCacheConfig.isLocalCacheSwitch() && LocalCacheTypeEnum.empty != methodCache.localCacheType()) {
            return true;
        }
        return false;
    }

    private boolean okDistributeCache(MethodCache methodCache) {
        if (DistributeCacheTypeEnum.empty == methodCache.distributeCacheType()) {
            return false;
        }
        if (getDistributeCache(methodCache) == null) {
            return false;
        }
        return true;
    }

    private DistributeCache getDistributeCache(MethodCache methodCache) {
        return DistributeCacheManager.getCache(methodCache.distributeCacheType());
    }

    private String getMethodCacheKey(Method method, Object[] args, String className) {
        return "cache_method_"+className+"_"+method.getName();
    }
}
