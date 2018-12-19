package com.woowle.sugarcoatedhaws.common.aspect;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.VO.ResultCode;
import com.woowle.sugarcoatedhaws.common.annotation.AffinityNotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

/**
 * Create By xiaoyin.lu.o on 2018/6/21
 **/
@Aspect
@Component
public class NotNullAspect {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Around(value = "@annotation(com.woowle.sugarcoatedhaws.common.annotation.AffinityNotNull)")
  public Object notNull(ProceedingJoinPoint joinPoint) {

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取连接点的方法签名对象；
    Method method = signature.getMethod();
    String methodName = signature.getName();
    Object controller = joinPoint.getTarget();// 获取连接点所在的目标对象；
    String controllerName = controller.getClass().getName();
    Object[] args = joinPoint.getArgs();
    LocalVariableTableParameterNameDiscoverer u =
        new LocalVariableTableParameterNameDiscoverer();
    String[] argsName = u.getParameterNames(method);

    Map<String, Object> params = Maps.newLinkedHashMap();
    for (int i = 0; i < args.length; i++) {
      params.put(argsName[i], args[i]);
    }
    AffinityNotNull require = method.getAnnotation(AffinityNotNull.class);
    if (StringUtils.isNotBlank(require.value())) {
      String fieldNames = require.value().replace("，", ",");
      for (String field : fieldNames.split(",")) {
        if (field.contains(".")) {
          String[] field_ = field.split("\\.");
          Object o = params.get(field_[0]);
          if (null == o) {
            logger.info("AffinityNotNull ： class : {} ,method {} ", controllerName, methodName);
            return Result.failed(ResultCode.ERROR_0002,
                String.format("[%s] must not be null ! (info From AffinityNotNull)", field));
          }
          for (int i = 1; i < field_.length; i++) {
            if (o instanceof Map) {
              Object o1 = ((Map) o).get(field_[i]);
              if (NullOrNot(methodName, controllerName, o1, field_[i])) {
                return Result.failed(ResultCode.ERROR_0002,
                    String.format("[%s] must not be null ! (info From AffinityNotNull)", field));
              }
              o = o1;
            }
            if (o instanceof JsonObject) {
              Object o1 = ((JsonObject) o).get(field_[i]);

              if (NullOrNot(methodName, controllerName, o1, field_[i])) {
                return Result.failed(ResultCode.ERROR_0002,
                    String.format("[%s] must not be null ! (info From AffinityNotNull)", field));
              }
              o = o1;
            } else {
              try {
                Method method1 = o.getClass().getDeclaredMethod("get" + captureName(field_[i]));
                method1.setAccessible(true);
                Object oi = method1.invoke(o);
                if (NullOrNot(methodName, controllerName, oi, field_[i])) {
                  return Result
                      .failed(ResultCode.ERROR_0002,
                          String
                              .format("[%s] must not be null ! (info From AffinityNotNull)",
                                  field));
                }
                o = oi;
              } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.info("{} is`t suitable for Affinity", field);
                return Result
                    .failed(ResultCode.ERROR_0002, "something is can`t be used in Affinity");
              }
            }
          }
        } else {
          Object o = params.get(field);
          if (NullOrNot(methodName, controllerName, o, field)) {
            return Result
                .failed(ResultCode.ERROR_0002,
                    String.format("[%s] must not be null ! (info From AffinityNotNull)", field));
          }
        }
      }
    }
    try {
      return joinPoint.proceed();
    } catch (IllegalArgumentException e) {
      logger.info("find a throwAble,class:{},method:{},throwable:{}", controllerName, methodName,
          e);
      return Result.failed(ResultCode.ERROR_0002, e.getMessage());
    } catch (Throwable throwable) {
      logger.info("find a throwAble,class:{},method:{},throwable:{} {}", controllerName, methodName,
          throwable, throwable.getStackTrace());
      return Result.failed(ResultCode.ERROR_0002, throwable.toString());
    }
  }

  private boolean NullOrNot(String methodName, String controllerName, Object o1, String field) {
    if (null == o1 || (o1 instanceof String && StringUtils.isBlank((String) o1))) {
      logger.info("AffinityNotNull ： class : {} ,method {} , reason:[{}] is null  ", controllerName,
          methodName, field);
      return true;
    }
    return false;
  }


  private String captureName(String str) {
    char[] cs = str.toCharArray();
    if (cs[0] <= 96) {
      return str;
    }
    cs[0] -= 32;
    return String.valueOf(cs);
  }


}
