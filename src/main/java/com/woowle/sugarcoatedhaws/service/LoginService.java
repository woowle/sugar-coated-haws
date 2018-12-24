package com.woowle.sugarcoatedhaws.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.constant.RedisKeyConstants;
import com.woowle.sugarcoatedhaws.common.util.RedisUtil;
import com.woowle.sugarcoatedhaws.common.util.RequestUtil;
import com.woowle.sugarcoatedhaws.common.util.UUIDHexGenerator;
import com.woowle.sugarcoatedhaws.crypto.AESHelper;
import com.woowle.sugarcoatedhaws.crypto.SaltCode;
import com.woowle.sugarcoatedhaws.mapper.UserMapper;
import com.woowle.sugarcoatedhaws.model.User;
import com.woowle.sugarcoatedhaws.model.request.LoginRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@Transactional
@Service
public class LoginService{
  @Autowired
  private UserMapper userMapper;



  public Result login(LoginRequest loginRequest,HttpServletRequest request){
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
      RedisUtil redisUtil = new RedisUtil();
      String ip = RequestUtil.getIpAddr(request);
      if(redisUtil.hasKey(RedisKeyConstants.USER_LIST+loginRequest.getUserName())) {
        String token = (String) redisUtil
            .get(RedisKeyConstants.USER_LIST + loginRequest.getUserName());
        String jsonStr = AESHelper.decryptEZ(token, user.getSalt());
        JSONObject json = JSONObject.parseObject(jsonStr);
        String ipOld = json.getString("ip");
        if (ipOld.equalsIgnoreCase(ip)) {
          redisUtil
              .expire(RedisKeyConstants.USER_LIST + loginRequest.getUserName(), 1000 * 60 * 30);
          redisUtil.expire(RedisKeyConstants.USER_TOKEN + token, 1000 * 60 * 30);
          return Result.success(token);
        }
      }
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis());
        json.put("userName", loginRequest.getUserName());
        json.put("ip", ip);
        json.put("random", SaltCode
            .getSalt(String.valueOf(Math.atan2(Math.random() * 10000, (Math.random() * 10000)))));
        String token = String.valueOf(AESHelper.encryptEZ(json.toJSONString(), user.getSalt()));
        new RedisUtil().set(RedisKeyConstants.USER_TOKEN + token, user.getSalt(), 1000 * 60 * 30);
        new RedisUtil()
            .set(RedisKeyConstants.USER_LIST + loginRequest.getUserName(), token, 1000 * 60 * 30);
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
     user.setAccountId(Math.abs(UUIDHexGenerator.toIntExt(userName.getBytes())));
     user.setRole(1);
     userMapper.insert(user);
     LoginRequest loginRequest = new LoginRequest();
     loginRequest.setUserName(userName);
     loginRequest.setPassword(password);
     return login(loginRequest,request);
    }
    return Result.failed("0001","该用户已被注册！");
  }

  public Result logout(HttpServletRequest request,String userName){
    if(StringUtils.isBlank(userName)){
      return Result.failed("0001","信息不全");
    }
    User user = userMapper.selectOne(new QueryWrapper<>(new User(userName)));
    if(null == user){
      return Result.failed("0001","error");
    }
    String token = request.getHeader("token");
    new RedisUtil().expire(RedisKeyConstants.USER_TOKEN+token,1);
    new RedisUtil().expire(RedisKeyConstants.USER_LIST+userName,1);
    return Result.success();
  }


}
