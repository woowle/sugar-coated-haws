package com.woowle.sugarcoatedhaws.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 集群定时任务配置注解
 * @author yuanlong.huang.o
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskClusterRedisConfig {

	/**redis存储对应的key*/
	String key() ;
	
	/**key对应的value值*/
	String value() default "1";
	
	/**
	 * 选用第一个redis库存储该key
	 * spring操作reids不支持切换数据库，
	 * 如果用的是spring操作reids，该配置不生效
	 * */
	String index() default "0";
	
	/**
	 * 该key的过期时间，单位秒，过期时间不能超过定时任务的时间间隔
	 * 过期时间只要大于各个集群节点redis执行setnx的时间和
	 * 一般可以设置为定时任务的时间间隔减去定时任务的一个时间单位
	 * 例如：@Scheduled(cron = "0/20 * * * * ?") timeout可以设置为19
	 * */
	int timeout() ;
	
	/**是否允许多节点并发运行 */
	boolean isConcurrent() default false;
	
}
