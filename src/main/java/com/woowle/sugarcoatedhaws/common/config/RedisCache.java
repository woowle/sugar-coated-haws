package com.woowle.sugarcoatedhaws.common.config;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache implements Cache {

  private static Logger logger = LoggerFactory.getLogger(RedisCache.class);//日志管理
  private String id;
  private RedisTemplate redisTemplate = RedisConfig.getRedis();
  private static final String MYBATIS_2ND_CACHE = "Mybatis2ndCache:";

  // 创建一个jedis连接
  private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); //读写锁

  public void setReadWriteLock(ReadWriteLock readWriteLock) {
    this.readWriteLock = readWriteLock;
  }

  public RedisCache(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Cache instance requires an ID");
    }
    logger.debug("create an cache instance with id" + id);
    this.id = id;
  }

  public String getId() {
    return this.id;
  }


  public void putObject(Object key, Object value) {
    if (null != redisTemplate) {
      String ketStr = MYBATIS_2ND_CACHE + key.toString();
      redisTemplate.opsForValue().set(ketStr, value, 2, TimeUnit.MINUTES);
    }
  }

  public Object getObject(Object key) { //缓存穿透
    if (null != redisTemplate) {
      //算法：计算一定时间内没有命中的键，存起来 key->value
      String ketStr = MYBATIS_2ND_CACHE + key.toString();
      if (redisTemplate.hasKey(ketStr)) {
        return redisTemplate.opsForValue().get(ketStr);
      }
    }
    return null;
  }

  public Object removeObject(Object key) {
    if (null != redisTemplate) {
      String ketStr = MYBATIS_2ND_CACHE + key.toString();
      return redisTemplate.expire(ketStr, 0, TimeUnit.MILLISECONDS);
    }
    return null;
  }

  public void clear() {
    if (null != redisTemplate) {
      redisTemplate.delete(MYBATIS_2ND_CACHE);
    }
  }

  public int getSize() {
    if (null != redisTemplate) {
      Long size = redisTemplate.opsForList().size(MYBATIS_2ND_CACHE);
      return Integer.valueOf(size + "");
    }
    return 0;
  }

  public ReadWriteLock getReadWriteLock() {
    return readWriteLock;
  }
}
