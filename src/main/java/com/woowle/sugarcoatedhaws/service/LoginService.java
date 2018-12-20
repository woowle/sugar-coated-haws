package com.woowle.sugarcoatedhaws.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.util.RequestUtil;
import com.woowle.sugarcoatedhaws.common.util.UUIDHexGenerator;
import com.woowle.sugarcoatedhaws.crypto.SaltCode;
import com.woowle.sugarcoatedhaws.mapper.UserMapper;
import com.woowle.sugarcoatedhaws.model.User;
import com.woowle.sugarcoatedhaws.model.request.LoginRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@Transactional
@Service
public class LoginService{
  @Autowired
  private UserMapper userMapper;


  public Result login(LoginRequest loginRequest){
    if(StringUtils.isBlank(loginRequest.getUserName())||StringUtils.isBlank(loginRequest.getPassword())){
      return Result.failed("0001","信息不全");
    }
    User user = userMapper.selectOne(new QueryWrapper<>(new User(loginRequest.getUserName())));
    if( null == user){
      return Result.failed("0001","没有该用户，请注册");
    }
    String salt = user.getSalt();
    String sugar = user.getSugar();
    if(SaltCode.check(loginRequest.getPassword(),sugar,salt)){
      JSONObject json = new JSONObject();
      json.put("time",System.currentTimeMillis());
      json.put("userName",loginRequest.getUserName());
      String token = DigestUtils.md5DigestAsHex(json.toJSONString().getBytes());
      return Result.success(token);
    }else{
      return Result.failed("0001","密码错误");
    }
  }

  public Result register(String userName,String password, HttpServletRequest request){
    if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
      return Result.failed("0001","信息不全");
    }
    User user = userMapper.selectOne(new QueryWrapper<>(new User(userName)));
    if(null == user){
     String salt = SaltCode.getSalt(password);
     user = new User();
     user.setUserName(userName);
     user.setSalt(salt);
     String sugar = SaltCode.chaos(password,salt);
     user.setSugar(sugar);
     user.setId(new UUIDHexGenerator(RequestUtil.getIpAddr(request)).generate());
     user.setAccountId(Math.abs(UUIDHexGenerator.toInt(userName.getBytes())));
     user.setRole(1);
     userMapper.insert(user);
     LoginRequest loginRequest = new LoginRequest();
     loginRequest.setUserName(userName);
     loginRequest.setPassword(password);
     return login(loginRequest);
    }
    return Result.failed("0001","该用户已被注册！");
  }


}
