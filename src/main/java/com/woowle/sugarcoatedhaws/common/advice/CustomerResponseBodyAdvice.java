package com.woowle.sugarcoatedhaws.common.advice;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.common.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Create By xiaoyin.lu.o at 2018/03/19 在返回值中插入RequestId，RequestId 从Attribute中获取"requestId".
 * 适用于标有注解为@ResponseBody 的接口 或 标有@RestController 的类 返回值必须为Result类型
 */


@ControllerAdvice
public class CustomerResponseBodyAdvice implements ResponseBodyAdvice {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    //false 为不拦截，true为拦截。可更具需求更改
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof Result) {
      ServletServerHttpRequest req = (ServletServerHttpRequest) request;
      String ip = RequestUtil.getIpAddr(req.getServletRequest());
      String uri = RequestUtil.trimURI(req.getServletRequest());
      ((Result) body).setRequestId(req.getServletRequest().getAttribute("requestId").toString());
      logger.info(
          "ip地址{},请求方式{},访问地址{},application={},appName={},loginAccount={},requestId= {},result = {}",
          ip, request.getMethod(), uri,
          req.getServletRequest().getParameter("application"),
          req.getServletRequest().getParameter("appName"),
          req.getServletRequest().getParameter("appName"),
          req.getServletRequest().getParameter("loginAccount"),
          req.getServletRequest().getAttribute("requestId"), body);
      return body;
    } else {
      return body;
    }
  }
}
