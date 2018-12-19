package com.woowle.sugarcoatedhaws.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create By xiaoyin.lu.o on 2018/10/10
 **/
@Target({ElementType.TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AffinityRetry {
  int time() default 1;
}
