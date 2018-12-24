package com.woowle.sugarcoatedhaws.controller;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.model.User;
import com.woowle.sugarcoatedhaws.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {
  @Autowired
  private UserService userService;

  @PostMapping("/1")
  public Result api(String id){
    User user =  userService.cache(id);
    log.info("result :{}",user.toString());

    return Result.success(user);
  }
}
