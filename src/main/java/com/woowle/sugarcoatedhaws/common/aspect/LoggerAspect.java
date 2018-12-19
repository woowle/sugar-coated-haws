package com.woowle.sugarcoatedhaws.common.aspect;

import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

/**
 * 注解日志切面
 *
 * @author jiangao.xia.o
 */
@Aspect
@Component
public class LoggerAspect {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * AffinityLogger create on 0612
   */
  @Before(value = "@within(com.woowle.sugarcoatedhaws.common.annotation.AffinityLogger) || @annotation(com.woowle.sugarcoatedhaws.common.annotation.AffinityLogger)")
  public void AffinityLogger(JoinPoint joinPoint) {
    StringBuilder stringBuilder = new StringBuilder();
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取连接点的方法签名对象；
    Method method = signature.getMethod();
    String methodName = signature.getName();
    Object controller = joinPoint.getTarget();// 获取连接点所在的目标对象；
    String controllerName = controller.getClass().getName();
    stringBuilder.append(controllerName).append(" | ").append(methodName).append("() ")
        .append(" param :");
    LocalVariableTableParameterNameDiscoverer u =
        new LocalVariableTableParameterNameDiscoverer();
    String[] params = u.getParameterNames(method);
    Object[] args = joinPoint.getArgs();
    List<String> sts = Lists.newArrayList();
    for (Object o : args) {
      if(null == o){
        sts.add("null");
      }else {
        try {
          sts.add(o.toString());
        }catch (Exception e){
          sts.add("UNKNOW");
        }
      }
    }
    Object[] obs = sts.toArray();
    for (String p : params) {
      stringBuilder.append(p).append("= {} , ");
    }
    stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length() - 2);
    logger.info(stringBuilder.toString(), obs);
//		AffinityLogger myLogger = method.getAnnotation(AffinityLogger.class);// 获取注解

  }
}
