package com.woowle.sugarcoatedhaws.controller;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.model.request.LoginRequest;
import com.woowle.sugarcoatedhaws.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@RestController
@RequestMapping("")
public class LoginController {
  @Autowired
  public LoginService loginService;

  @PostMapping("/login")
  public Result login(LoginRequest loginRequest){
    return loginService.login(loginRequest);
  }

  @PostMapping("/register")
  public Result register(String userName,String password, HttpServletRequest request){
    return loginService.register(userName,password,request);
  }

}
