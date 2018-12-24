package com.woowle.sugarcoatedhaws.common.config;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis序列化配置
 */
@Configuration
@EnableCaching
public class RedisConfig {

  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private LettuceConnectionFactory lettuceConnectionFactory;

  private static RedisTemplate redisTemplateSta;
  private static LettuceConnectionFactory lettuceConnectionFactorySta;

  @Bean
  public RedisTemplate<Object, Object> redisTemplate() {
    redisTemplate.setConnectionFactory(lettuceConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    //value hashmap序列化
    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    return redisTemplate;
  }


  @Bean
  CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    //初始化一个RedisCacheWriter
    RedisCacheWriter redisCacheWriter = RedisCacheWriter
        .nonLockingRedisCacheWriter(connectionFactory);
    //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
//    ClassLoader loader = this.getClass().getClassLoader();
//    JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
//    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
//    RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
    RedisCacheConfiguration defaultCacheConfig = getDefaultCacheConfig(Duration.ofHours(1));
    Map<String, RedisCacheConfiguration> initCacheConfig = Maps.newHashMap();
    Map<String, Long> map = getCustomerExpires();
    map.forEach(new BiConsumer<String, Long>() {
      @Override
      public void accept(String s, Long aLong) {
        initCacheConfig.put(s,getDefaultCacheConfig(Duration.ofMillis(aLong)));
      }
    });

    //初始化RedisCacheManager
    RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig,initCacheConfig);

    return cacheManager;
  }


  private RedisCacheConfiguration getDefaultCacheConfig(Duration duration) {

    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(duration)
        .disableCachingNullValues().serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer())).computePrefixWith(getCacheKeyPrefix());
  }

  private CacheKeyPrefix getCacheKeyPrefix(){
    CacheKeyPrefix cacheKeyPrefix = new CacheKeyPrefix() {
      @Override
      public String compute(String cacheName) {
        return cacheName + ":";
      }
    };
    return cacheKeyPrefix;
  }


  private static Map<String, Long> getCustomerExpires() {
    Map<String, Long> customerExpires = Maps.newConcurrentMap();
    // TODO: 2018/4/23 添加Key，Long 代表 存入缓存的Key和过期时间
    return customerExpires;
  }


  @Bean(name = "SimpleKey")
  public KeyGenerator getKeyGenerator() {
    return (target, method, params) -> {
      StringBuilder sb = new StringBuilder();
      sb.append(target.getClass().getName() + ":");
      sb.append(method.getName() + ":");
      for (Object obj : params) {
        sb.append(obj.toString());
      }
      return sb.toString();
    };
  }

  @PostConstruct
  private void init() {
    redisTemplateSta = redisTemplate;
    lettuceConnectionFactorySta = lettuceConnectionFactory;
  }

  public static RedisTemplate getRedis() {
    return redisTemplateSta;
  }

  public static LettuceConnectionFactory getFactory() {
    return lettuceConnectionFactorySta;
  }
}