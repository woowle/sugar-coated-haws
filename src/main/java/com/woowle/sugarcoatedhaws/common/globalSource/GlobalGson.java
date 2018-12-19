package com.woowle.sugarcoatedhaws.common.globalSource;

import com.google.gson.Gson;

/**
 * Create By xiaoyin.lu.o on 2018/12/7
 **/
public class GlobalGson {
  private static Gson gson;

  static {
    gson = new Gson().newBuilder().enableComplexMapKeySerialization().serializeNulls()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting() //对结果进行格式化，增加换行
        .disableHtmlEscaping().create(); //防止特殊字符出现乱码;
  }

  public static Gson Gson(){
    return gson;
  }

}
