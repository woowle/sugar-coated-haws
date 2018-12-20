package com.woowle.sugarcoatedhaws;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.woowle.sugarcoatedhaws.common.globalSource.GlobalGson;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class,scanBasePackages = "com.woowle.sugarcoatedhaws")
@MapperScan("com.woowle.sugarcoatedhaws.mapper")
public class SugarCoatedHawsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SugarCoatedHawsApplication.class, args);
  }

  @Bean
  public HttpMessageConverters fastJsonHttpMessageConverters() {
    // 1.定义一个converters转换消息的对象
    GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
//    Gson gson = new Gson().newBuilder().enableComplexMapKeySerialization().serializeNulls()
//        .setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting() //对结果进行格式化，增加换行
//        .disableHtmlEscaping().create(); //防止特殊字符出现乱码;
    // 3.在converter中添加配置信息
    gsonHttpMessageConverter.setGson(GlobalGson.Gson());
    // 4.将converter赋值给HttpMessageConverter
    HttpMessageConverter<?> converter = gsonHttpMessageConverter;
    // 5.返回HttpMessageConverters对象
    return new HttpMessageConverters(converter);
  }


}

