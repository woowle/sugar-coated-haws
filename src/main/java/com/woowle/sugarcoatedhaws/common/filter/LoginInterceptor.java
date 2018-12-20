package com.woowle.sugarcoatedhaws.common.filter;

import com.google.common.collect.Maps;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Filter;
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
    return true;
  }

}
