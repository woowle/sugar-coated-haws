package com.woowle.sugarcoatedhaws.common.util;

import java.math.BigDecimal;

/**
 * Create By xiaoyin.lu.o on 2018/12/11
 **/
public class CommonUtil {
  static class FloatUtil{
    public static boolean in(float source,float min,float max){
      return source > min && source < max;
    }

    public static boolean eIn(float source,float min,float max){
     return source >= min && source <= max;
    }

    public static float setDeNum(float source ,int i ){
      BigDecimal decimal = new BigDecimal(source);
      decimal = decimal.setScale(i,BigDecimal.ROUND_DOWN);
      return decimal.floatValue();
    }


  }

}
