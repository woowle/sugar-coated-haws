package com.woowle.sugarcoatedhaws.common.filter;

import com.woowle.sugarcoatedhaws.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurationSupport {
  @Autowired
  private LoginInterceptor loginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ServerInterceptor()).addPathPatterns("/**");
    registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register").excludePathPatterns("/error");

    super.addInterceptors(registry);
  }

}