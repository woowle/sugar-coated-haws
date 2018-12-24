package com.woowle.sugarcoatedhaws.mapper;

import com.woowle.sugarcoatedhaws.model.User;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
@CacheNamespace(implementation=com.woowle.sugarcoatedhaws.common.config.RedisCache.class)
public interface UserMapper extends Mapper<User> {

}
