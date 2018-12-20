package com.woowle.sugarcoatedhaws.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.springframework.util.DigestUtils;

/**
 * Create By xiaoyin.lu.o on 2018/5/31
 **/
public class SaltCode {

  public static boolean check(String code,String hashCode,String salt){
    return hashCode.equalsIgnoreCase(chaos(code,salt));
  }

  public static String chaos(String code,String salt){
    char[] code_ = code.toCharArray();
    char[] salt_ = salt.toCharArray();
    return DigestUtils.md5DigestAsHex(String.valueOf(fusion(code_,salt_)).getBytes()).toUpperCase();
  }

  public static String getSalt(String code){
    int byteLength = code.length();
    SecureRandom rnd = new SecureRandom();
    byte[] token = new byte[byteLength];
    rnd.nextBytes(token);
    return new BigInteger(1, token).toString(16); //hex encoding
  }

  private static char[] fusion(char[] a , char[] b){
    char[] r = new char[a.length+b.length];
    int j=0;
    for(int i =0;i<(a.length>b.length?a.length:b.length);i++){
      if(i<=a.length-1) {
        r[j] = a[i];
        j++;
        if(i<=b.length-1) {
          r[j] = b[i];
          j++;
        }
      }else{
        r[j] = b[i];
        j++;
      }
    }
    return r;
  }
}
