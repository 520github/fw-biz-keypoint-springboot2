package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.aspect;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.annotation.DistributeLock;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.exception.DistributeLockException;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.executer.DistributeLockExecuter;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler.DistributeLockHandler;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.model.DistributeLockModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Aspect
public class DistributeLockAspect {

    public static final String PLACE_HOLDER = "${";

    public final static Pattern KEY_GROUP = Pattern.compile("\\$\\{([^}]*)\\}");

    @Autowired
    private DistributeLockHandler distributeLockHandler;

    public DistributeLockAspect() {

    }

    public DistributeLockAspect(DistributeLockHandler distributeLockHandler) {
        this.distributeLockHandler = distributeLockHandler;
    }

    @Pointcut("@annotation(org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.annotation.DistributeLock)")
    public void distributedLockAnnotationPointcut() {
    }

    @Around(value = "distributedLockAnnotationPointcut()")
    public Object aroundDistributeLock(ProceedingJoinPoint point) {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            String className = point.getTarget().getClass().getName();

            log.info("DistributeLockAspect aroundDistributeLock className[{}], methodName[{}]", className, method.getName());

            DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

            log.info("DistributeLockAspect DistributeLock[{}]", distributeLock);
            if (StrUtil.isBlank(distributeLock.lockKey())) {

            }
            log.info("DistributeLockAspect get lockModel");
            DistributeLockModel lockModel = getDistributeLockModel(distributeLock);
            log.info("DistributeLockAspect lockModel origin[{}]", lockModel);
            //判断分布式锁key是否有占位符需要处理
            if (lockModel.getLockKey().contains(PLACE_HOLDER)) {
                lockModel.setLockKey(getDistributeKey(lockModel.getLockKey(), point));
            }

            log.info("DistributeLockAspect lockModel replace[{}]", lockModel);
            return distributeLockHandler.handle(lockModel, new DistributeLockExecuter() {
                @Override
                public Object execute() {
                    try {
                        log.info("DistributeLockAspect execute lockModel[{}]", lockModel);
                        return point.proceed();
                    } catch (Throwable e) {
                        throw new DistributeLockException("", e);
                    }
                }
            });
        }catch (Exception e) {
            log.error("DistributeLockAspect exception", e);
        }
        return null;
    }

    private String getDistributeKey(String lockKey, ProceedingJoinPoint pjp) {
        if (pjp.getArgs() == null || pjp.getArgs().length == 0) {
            throw new DistributeLockException("");
        }
        Object firstArg = getFirstArgObject(pjp);
        log.info("DistributeLockAspect firstArg[{}]", firstArg);
        if (firstArg == null) {
            throw new DistributeLockException("");
        }
        List<String> matchKeyList = ReUtil.findAll(KEY_GROUP, lockKey, 0);
        JSONObject jsonObject = new JSONObject(firstArg);
        log.info("DistributeLockAspect jsonObject[{}]", jsonObject);
        for(String matchKey: matchKeyList) {
            log.info("DistributeLockAspect matchKey[{}]", matchKey);
            matchKey = matchKey.replace(PLACE_HOLDER, "").replace("}", "");
            String replaceValue = jsonObject.getByPath(matchKey, String.class);
            log.info("DistributeLockAspect get value[{}] by key[{}]", replaceValue, matchKey);
            if (StrUtil.isNotBlank(replaceValue)) {
                lockKey = lockKey.replace(PLACE_HOLDER+matchKey+"}", replaceValue);
            }
        }
        log.info("after replace lockKey[{}]", lockKey);
        return lockKey;
    }

    private Object getFirstArgObject(ProceedingJoinPoint pjp) {
        for(Object arg: pjp.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            return arg;
        }
        return null;
    }

    private DistributeLockModel getDistributeLockModel(DistributeLock distributeLock) {
        DistributeLockModel model = new DistributeLockModel();
        model.setLockKey(distributeLock.lockKey());
        model.setWaitTime(distributeLock.waitTime());
        model.setLockTypeEnum(distributeLock.lockType());
        model.setExpireTime(distributeLock.expireTime());
        model.setTimeUnit(distributeLock.timeUnit());
        return model;
    }

}
