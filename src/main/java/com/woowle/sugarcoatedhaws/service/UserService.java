package com.woowle.sugarcoatedhaws.service;

import com.woowle.sugarcoatedhaws.mapper.UserMapper;
import com.woowle.sugarcoatedhaws.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Create By xiaoyin.lu.o on 2018/12/24
 **/
@Service
public class UserService {
  @Autowired
  private UserMapper userMapper;

  @Cacheable(value = "users",key = "#id")
  public User cache(String id){
    return userMapper.selectById(id);
  }

}
