package com.woowle.sugarcoatedhaws.common.VO;

import com.google.common.collect.Maps;
import com.woowle.sugarcoatedhaws.common.annotation.Sub;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Create By xiaoyin.lu.o on 2018/12/3
 **/
public class
ResultCode {


  private static Map<String, String> resultMap = Maps.newHashMap();



  static {
    Field[] fields = ResultCode.class.getDeclaredFields();
    for (Field field : fields) {
      Object o = null;
      try {
        o = field.get(ResultCode.class);
      } catch (IllegalAccessException ignored) {
      }
      if(o instanceof String) {
        Annotation ano = field.getAnnotation(Sub.class);
        if(null == ano){
          resultMap.put(o.toString(), "");
        }else {
          Sub sub = (Sub) ano;
          resultMap.put(o.toString(), sub.name());
        }
      }
    }
  }

  public static String getValueByKey(String key) {
    return resultMap.getOrDefault(key, "");
  }

  	private static String getValueByKey1(String key) {
		ResultCode mySub = new ResultCode();
		if (key == null || key.equals("")) {
			key = "0000";
		}
		String result = "Success";
		Field[] fields = mySub.getClass().getDeclaredFields();
		for (Field field : fields) {
			Annotation ano = field.getAnnotation(Sub.class);
			try {
				if (key.equals(field.get(mySub).toString()) && ano != null) {
					Sub sub = (Sub) ano;
					result = (String) sub.name();
					break;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return result;
	}

/*=====================================================================*/
  @Sub(name = "0001")
  public static final String ERROR_0001 = "0001";
  public static final String ERROR_0002 = "0002";

  public static void main(String[] str){
    long time = System.currentTimeMillis();
    for(long i = 0 ; i<=1000000000L; i++){
      ResultCode.getValueByKey("0001");
    }
    System.out.println(System.currentTimeMillis()-time);
    time = System.currentTimeMillis();
    for(long i = 0 ; i<=100000000L; i++){
      ResultCode.getValueByKey1("0001");
    }
    System.out.println(System.currentTimeMillis()-time);
  }

}
