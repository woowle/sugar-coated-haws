package com.woowle.sugarcoatedhaws.controller;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@RestController
@RequestMapping("/api")
public class ApiController {

  @PostMapping("/1")
  public Result api(){
    return Result.success();
  }
}
