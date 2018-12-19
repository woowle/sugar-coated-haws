package com.woowle.sugarcoatedhaws.common.filter;

import com.woowle.sugarcoatedhaws.common.util.RequestUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ServerInterceptor implements HandlerInterceptor {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String TIME_LOG_NAME = "vom-method-time-consuming-name";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String ip = RequestUtil.getRemoteAddr(request);
    String uri = RequestUtil.trimURI(request);
    String requestId = RequestUtil.resolveReqId(ip);
    request.setAttribute("requestId", requestId);
    MDC.put("requestId", requestId);
    logger.info("ip地址{},请求方式{},访问地址{},application={},appName={},loginAccount={},requestId= {}", ip,
        request.getMethod(), uri,
        request.getParameter("application"), request.getParameter("appName"),
        request.getParameter("loginAccount"), requestId);
    request.setAttribute(TIME_LOG_NAME, System.currentTimeMillis());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    long endTime = System.currentTimeMillis();
    long startTime = (Long) request.getAttribute(TIME_LOG_NAME);
    long time = endTime - startTime;
    if (time > 200) {
      logger.warn("method to detect timeout for {}, and the execution time is {} ms",
          RequestUtil.trimURI(request), time);
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex)
      throws Exception {
    // System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet
    // 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
  }

}