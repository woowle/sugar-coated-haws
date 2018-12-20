package com.woowle.sugarcoatedhaws.common.util;

import com.google.common.collect.Maps;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;


public class RequestUtil {

  /**
   * 判断uri是不是以字符串数组中的元素为前缀
   */
  public static boolean isStartsWith(String uri, String prefixArray) {
    if (StringUtils.isNotBlank(prefixArray)) {
      for (String prefix : prefixArray.split(",")) {
        if (uri.startsWith(prefix)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 判断uri是不是存在于字符串数组中
   */
  public static boolean isEqualsIn(String uri, String uriArray) {
    if (StringUtils.isNotBlank(uriArray)) {
      for (String s : uriArray.split(",")) {
        if (uri.equals(s)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 对uri中可能出现的多个斜杠去重
   */
  public static String trimURI(HttpServletRequest request) {
    String uri = request.getRequestURI();
    StringBuffer sb = new StringBuffer();
    boolean flag = false;
    for (int i = 0; i < uri.length(); i++) {
      char c = uri.charAt(i);
      if (c == '/') {
        if (flag) {
          continue;
        }
        flag = true;
      } else {
        flag = false;
      }
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * 获取ip地址
   */
  public static String getRemoteAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    if (ip.split(",").length > 1) {
      ip = ip.split(",")[0];
    }
    return ip;
  }

  /**
   * 获取request中所有参数
   */
  public static Map<String, String> getAllParameter(HttpServletRequest request) {
    Map<String, String> map = Maps.newHashMap();
    Enumeration<String> enu = request.getParameterNames();
    while (enu.hasMoreElements()) {
      String paraName = enu.nextElement();
      String value = request.getParameter(paraName);
      if (value != null) {
        try {
          value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
      }
      map.put(paraName, value);
    }
    return map;
  }


  public static String resolveReqId(String ip) {
    // 规则： hexIp(ip)base36(timestamp)-seq
    return hexIp(ip) + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + "-" + ran();
  }

  // 将ip转换为定长8个字符的16进制表示形式：255.255.255.255 -> FFFFFFFF
  private static String hexIp(String ip) {
    StringBuilder sb = new StringBuilder();

    if (ip.replaceAll("\\d", "").length() != 3) {
      for (String seg : ip.split(":")) {
        String h = Integer.toHexString(Integer.parseInt(seg));
        if (h.length() == 1) {
          sb.append("0");
        }
        sb.append(h);
      }
    } else {
      for (String seg : ip.split("\\.")) {
        String h = Integer.toHexString(Integer.parseInt(seg));
        if (h.length() == 1) {
          sb.append("0");
        }
        sb.append(h);
      }
    }
    return sb.toString();
  }

  private static StringBuilder ran() {
    int[] num = new int[4];
    for (int i = 0; i < 4; i++) {
      num[i] = (int) Math.floor(Math.random() * 90 + 10);

      for (int j = 0; j < i; j++) {
        while (num[i] == num[j]) {
          num[i] = (int) Math.floor(Math.random() * 90 + 10);
        }
      }
    }
    return new StringBuilder().append(Integer.toHexString(num[0]))
        .append(Integer.toHexString(num[1])).append(Integer.toHexString(num[2]))
        .append(Integer.toHexString(num[3]));
  }


  /**
   * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
   * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
   *
   * @return ip
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      if (ip.indexOf(",") != -1) {
        ip = ip.split(",")[0];
      }
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");

    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");

    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");

    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();

    }

    return ip;
  }


}
