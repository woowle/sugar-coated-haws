package com.woowle.sugarcoatedhaws.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD})
@Documented
public @interface Sub {

  String name() default "";
}