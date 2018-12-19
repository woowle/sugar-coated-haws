package com.woowle.sugarcoatedhaws.common.aspect;

import com.google.common.collect.Maps;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.VO.ResultCode;
import com.woowle.sugarcoatedhaws.common.annotation.AffinityRetry;
import java.lang.reflect.Method;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Create By xiaoyin.lu.o on 2018/10/10
 **/
@Aspect
@Component
public class AffinityRetryAspect {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  Map<String, Integer> retryCount = Maps.newConcurrentMap();

  @Around(value = "@within(com.woowle.sugarcoatedhaws.common.annotation.AffinityRetry) || @annotation(com.woowle.sugarcoatedhaws.common.annotation.AffinityRetry)")
  public Object AffinityRetry(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取连接点的方法签名对象；
    Method method = signature.getMethod();
    String methodName = signature.getName();
    Object controller = joinPoint.getTarget();// 获取连接点所在的目标对象；
    String controllerName = controller.getClass().getName();
    Thread current = Thread.currentThread();
    String currentName = current.getName();
    methodName = methodName + "-" + currentName;
    AffinityRetry affinityRetry = method.getAnnotation(AffinityRetry.class);
    int time = affinityRetry.time();

    try {
      Object object = joinPoint.proceed();
      resetRetryCount(methodName);
      return object;
    } catch (Throwable throwable) {
      if (couldRetry(methodName, time)) {
        logger.error("AffinityRetry: {} | {} : Error should retry.ERROR:{}", controllerName,
            methodName, throwable.getMessage());
        addRetryCount(methodName);
        return AffinityRetry(joinPoint);
      } else {
        logger
            .error("AffinityRetry: {} | {} : Error can not retry time max.ERROR:{}", controllerName,
                methodName, throwable.getMessage());
        resetRetryCount(methodName);
        return Result.failed(ResultCode.ERROR_0001);
      }
    }
  }

  private void addRetryCount(String method) {
    if (!retryCount.containsKey(method)) {
      retryCount.put(method, 2);
    } else {
      Integer i = retryCount.get(method);
      i = i + 1;
      retryCount.put(method, i);
    }
  }

  private void resetRetryCount(String method) {
    retryCount.remove(method);
  }

  private boolean couldRetry(String method, int count) {
    if (retryCount.containsKey(method)) {
      if (retryCount.get(method) > count) {
        return false;
      }
    }
    return true;
  }

}
