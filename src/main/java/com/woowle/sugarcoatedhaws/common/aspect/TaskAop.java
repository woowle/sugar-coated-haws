package com.woowle.sugarcoatedhaws.common.aspect;//package com.lux.affraity.common.aspect;
//
//import com.lux.affraity.common.annotation.TaskClusterRedisConfig;
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//import javax.annotation.Resource;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * 定时任务@Scheduled扫描注解
// *
// * @author yuanlong.huang.o
// *
// */
//@Aspect
//@Component
//public class TaskAop {
//
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	@Resource
//  RedisTemplate<Object, Object> redisTemplate;
//
//	@Pointcut("@annotation(com.nio.platform.annotation.TaskClusterRedisConfig)")
//	public void pointcut() {
//	}
//
//	@Around("pointcut()")
//	public void taskAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//		String methodName = methodSignature.getName();
//		String controllerName = joinPoint.getTarget().getClass().getName();
//		Method method = methodSignature.getMethod();
//		if (method.isAnnotationPresent(TaskClusterRedisConfig.class)) {
//			TaskClusterRedisConfig tc = method.getAnnotation(TaskClusterRedisConfig.class);
//			boolean isConcurrent = tc.isConcurrent();
//			if (!isConcurrent) {
//				String key = tc.key();
//				String value = tc.value();
//				int timeout = tc.timeout();
//				// 只有在 key 不存在时设置 key 的值 设置成功返回true,否则返回false
//				boolean flag = redisTemplate.opsForValue().setIfAbsent(key, value);
//				if (flag) {
//					redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
//					try {
//						joinPoint.proceed();
//					} catch (Throwable e) {
//						logger.info(controllerName + "-" + methodName + "定时任务失败,错误信息error={" + e.getMessage() + "}");
//						throw new RuntimeException(e);
//					}
//				} else {
//					return;
//				}
//			}else {
//				joinPoint.proceed();
//			}
//
//		} else {
//			joinPoint.proceed();
//		}
//	}
//
//}
