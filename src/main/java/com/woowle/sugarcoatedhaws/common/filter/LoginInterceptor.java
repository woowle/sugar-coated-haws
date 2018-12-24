package com.woowle.sugarcoatedhaws.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.constant.RedisKeyConstants;
import com.woowle.sugarcoatedhaws.common.util.RedisUtil;
import com.woowle.sugarcoatedhaws.crypto.AESHelper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Create By xiaoyin.lu.o on 2018/12/20
 **/
@Component
public class LoginInterceptor implements HandlerInterceptor {
  Map<String,Long> session = Maps.newConcurrentMap();

  @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String token = request.getHeader("token");
    if(StringUtils.isBlank(token)){
      response.setHeader("Content-type", "json/html;charset=UTF-8");
      response.getOutputStream().write(Result.failed("0001","请登录").toJson().getBytes(
          StandardCharsets.UTF_8));
      return false;
    }
    RedisUtil redisUtil = new RedisUtil();
    if(!redisUtil.hasKey(RedisKeyConstants.USER_TOKEN+token)){
      response.setHeader("Content-type", "json/html;charset=UTF-8");
      response.getOutputStream().write(Result.failed("0001","请登录").toJson().getBytes(
          StandardCharsets.UTF_8));
      return false;
    }
    String salt = (String) redisUtil.get(RedisKeyConstants.USER_TOKEN+token);
    String jsonStr = AESHelper.decryptEZ(token,salt);
    JSONObject json = JSONObject.parseObject(jsonStr);
    Long time = json.getLong("time");
    if(System.currentTimeMillis() - time > 1000*60*30 ){
      response.setHeader("Content-type", "json/html;charset=UTF-8");
      response.getOutputStream().write(Result.failed("0001","授权时间已到").toJson().getBytes(
          StandardCharsets.UTF_8));
      return false;
    }
    if(StringUtils.isNotBlank(request.getParameter("userName"))){
      if(request.getParameter("userName").equalsIgnoreCase(json.getString("userName"))){
        response.setHeader("Content-type", "json/html;charset=UTF-8");
        response.getOutputStream().write(Result.failed("0001","错误的授权").toJson().getBytes(
            StandardCharsets.UTF_8));
        return false;
      }
    }
    return true;
  }

}
