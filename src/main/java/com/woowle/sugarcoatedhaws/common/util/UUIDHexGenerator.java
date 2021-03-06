package com.woowle.sugarcoatedhaws.common.util;

import com.google.common.collect.Sets;
import java.net.InetAddress;
import java.util.Set;
import javax.xml.transform.Source;

public class UUIDHexGenerator {
    private String sep = "";
    private static  int IP;

    private static short counter = (short) 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

  public static int toIntExt(byte[] bytes) {
    int result = 0;
    for (int i = 0; i < bytes.length; i++) {
      result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
    }
    return result;
  }

    public UUIDHexGenerator(String ip){
      int ipadd;
      try {
        ipadd = toInt(ip.getBytes());
      } catch (Exception e) {
        ipadd = 0;
      }
      IP = ipadd;
    }
    /**
     * Unique in a local network
     */
    protected int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    protected short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    /**
     * Unique across JVMs on this machine (unless they load this class in the
     * same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public String generate() {
        return new StringBuilder(36).append(format(getIP())).append(sep)
                .append(format(getJVM())).append(sep)
                .append(format(getHiTime())).append(sep)
                .append(format(getLoTime())).append(sep)
                .append(format(getCount())).toString();
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there are >
     * Short.MAX_VALUE instances created in a millisecond)
     */
    protected short getCount() {
        synchronized (UUIDHexGenerator.class) {
          if (counter < 0) {
            counter = 0;
          }
            return counter++;
        }
    }

    public static void main(String[] str){
      System.out.println(toIntExt("123njnj".getBytes()));
      System.out.println(toIntExt("123njnj.1".getBytes()));
      System.out.println(toIntExt("dsadkjsaniqwnduwqndqwnjqndw".getBytes()));
    }
}
