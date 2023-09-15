package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.local.LocalCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.annotation.MethodCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.method.config.MethodCacheConfig;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCache;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.distribute.DistributeCacheManager;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.DistributeCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.enums.LocalCacheTypeEnum;
import org.sunso.keypoint.springboot2.biz.keypoint.cache.model.LocalCacheModel;
import org.sunso.keypoint.springboot2.spring.log.BaseLog;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Slf4j
public class MethodCacheAspect extends BaseLog {

    @Resource
    private MethodCacheConfig methodCacheConfig;

    @Pointcut("@annotation(org.sunso.keypoint.springboot2.biz.keypoint.cache.method.annotation.MethodCache)")
    public void methodCachePoint() {
    }

    @Around(value = "methodCachePoint()")
    public Object doMethodCache(ProceedingJoinPoint point) throws Throwable {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            String className = point.getTarget().getClass().getName();

            log.info("MethodCacheAspect doMethodCache className[{}], methodName[{}]", className, method.getName());

            MethodCache methodCache = method.getAnnotation(MethodCache.class);
            //获取缓存key
            String methodCacheKey = getMethodCacheKey(methodCache, method, point.getArgs(), className);

            //从缓存获取数据
            Object result = getDataFromCache(point, methodCache, methodCacheKey);
            if (result != null) {
                log.info("MethodCacheAspect doMethodCache get data from cache by className[{}], methodName[{}]", className, method.getName());
                return result;
            }

            //缓存无数据, 执行方法获取数据
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
            Object result = getLocalCache(methodCache).get(methodCacheKey, methodCache.expireTime(), methodCache.timeUnit());
            log.info("getDataFromCache get value[{}] from local cache by key[{}]", result, methodCacheKey);
            if (result != null) {
                return result;
            }
        }
        //分布式缓存为空，直接执行方法
        if (!okDistributeCache(methodCache)) {
            log.info("getDataFromCache not find any distribute cache by key[{}]", methodCacheKey);
            return point.proceed();
        }
        //从分布式缓存读取数据
        Object result = getDistributeCache(methodCache).get(methodCacheKey);
        if (result == null) {
            return null;
        }
        log.info("getDataFromCache get value[{}],className[{}] from distribute cache by key[{}]", result, result.getClass().getName(), methodCacheKey);
        //设置到本地缓存
        if (okLocalCache(methodCache)) {
            log.info("getDataFromCache set value[{}] to local cache key[{}]", result, methodCacheKey);
            getLocalCache(methodCache).set(methodCacheKey, result, methodCache.expireTime(), methodCache.timeUnit());
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
            log.info("getDataFromMethod set result[{}] to distribute cache[{}] by key[{}]", result, methodCache.distributeCacheType(), methodCacheKey);
        }
        if (okLocalCache(methodCache)) {
            getLocalCache(methodCache).set(methodCacheKey, result, methodCache.expireTime(), methodCache.timeUnit());
            log.info("getDataFromMethod set result[{}] to local cache[{}] by key[{}]", result, methodCache.localCacheType(), methodCacheKey);
        }
        return result;
    }

    /**
     * 判断是否开启本地缓存
     * @param methodCache
     * @return
     */
    private boolean okLocalCache(MethodCache methodCache) {
        //全局本地缓存开关是否打开
        if (!methodCacheConfig.isLocalCacheSwitch()) {
            return false;
        }
        //该方法是否支持本地缓存
        if (LocalCacheTypeEnum.empty == methodCache.localCacheType()) {
            return false;
        }
        //是否获取到本地缓存对象
        if (getLocalCache(methodCache) == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否开启分布式缓存
     * @param methodCache
     * @return
     */
    private boolean okDistributeCache(MethodCache methodCache) {
        if (DistributeCacheTypeEnum.empty == methodCache.distributeCacheType()) {
            return false;
        }
        if (getDistributeCache(methodCache) == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取分布式缓存
     * @param methodCache
     * @return
     */
    private DistributeCache getDistributeCache(MethodCache methodCache) {
        return DistributeCacheManager.getCache(methodCache.distributeCacheType());
    }

    /**
     * 获取本地缓存
     * @param methodCache
     * @return
     */
    private LocalCache getLocalCache(MethodCache methodCache) {
        return LocalCacheManager.getCache(methodCache.localCacheType(), LocalCacheModel.newInstance(methodCacheConfig));
    }

    /**
     * 获取缓存key
     * @param methodCache
     * @param method
     * @param args
     * @param className
     * @return
     */
    private String getMethodCacheKey(MethodCache methodCache, Method method, Object[] args, String className) {
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(methodCacheConfig.getBizServiceCachePrefixKey())) {
            sb.append(methodCacheConfig.getBizServiceCachePrefixKey());
        }
        sb.append(methodCacheConfig.getMethodCachePrefixKey());
        if (StrUtil.isNotBlank(methodCache.bizPrefixKey())) {
            sb.append(methodCache.bizPrefixKey()).append("_");
        }
        sb.append(getAboutMethodMd5Key(method, args, className));
        return sb.toString();
    }

    /**
     * 获取执行方法对应的md5值
     * @param method
     * @param args
     * @param className
     * @return
     */
    private String getAboutMethodMd5Key(Method method, Object[] args, String className) {
        StringBuilder sb = new StringBuilder();
        sb.append(className).append("&");
        sb.append(method.getName()).append("&");
        if (args != null) {
            for(Object obj: args) {
                sb.append(JSON.toJSONString(obj)).append("&");
            }
        }
        logInfoControl("", sb.toString());
        return MD5.create().digestHex(sb.toString());
    }
}
