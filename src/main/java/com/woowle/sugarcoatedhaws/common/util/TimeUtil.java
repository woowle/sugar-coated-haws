package com.woowle.sugarcoatedhaws.common.util;

import com.alibaba.druid.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author zhang
 */

public class TimeUtil {
	public static final String TIME_FORMAT_1 = "yyyyMMddHHmmss";
	
	public static final String TIME_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
	
	public static final String TIME_FORMAT_3 = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private static DateFormat timeFormat(String timeFormat) {
		return new SimpleDateFormat(StringUtils.isEmpty(timeFormat) ? TIME_FORMAT_1 : timeFormat);
	}
	
	public static Date addDate(Date date,int calendarType,int num) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(calendarType, num);    
	    return calendar.getTime();    
	}
	
	public static Date minusHour(Date date,int hour){
		 Calendar calendar = Calendar.getInstance();    
		    calendar.setTime(date);    
		    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);  
		    return calendar.getTime();    
	}

	public static String time2String(long time) {
		Date dat = new Date(time);
		return time2String(dat);
	}

	public static String time2String() {
		return time2String(new Date());
	}

	public static String time2String(Date date) {
		return time2String(date, "");
	}

	public static String time2String(long time, String timeFormat) throws ParseException {
		return time2String(time2Date(time), timeFormat);
	}

	public static String time2String(Date date, String timeFormat) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		DateFormat format = timeFormat(timeFormat);
		return format.format(gc.getTime());
	}

	public static long String2time(String dateStr) throws ParseException {
		return String2time(dateStr, TIME_FORMAT_2);
	}

	public static Date String2Date(String dateStr){
		return String2Date(dateStr,TIME_FORMAT_2);
	}
	
	public static Date String2Date(String dateStr,String timeFormat){
		Date date = null;
		if (!StringUtils.isEmpty(dateStr)) {
			try {
				date = new SimpleDateFormat(StringUtils.isEmpty(timeFormat) ? "yyyy-MM-dd HH:mm:SS" : timeFormat)
						.parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public static long String2time(String dateStr, String timeFormat) {
		Date date = String2Date(dateStr, timeFormat);
		return date==null?0:date.getTime();
	}

	public static Date time2Date(long time) throws ParseException {
		return time2Date(time, "yyyy-MM-dd HH:mm:SS");
	}

	public static Date time2Date(long time, String timeFormat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		String d = format.format(time);
		return format.parse(d);
	}

	public static int getYearOfDate(Date p_date) {
		Calendar c = Calendar.getInstance();
		c.setTime(p_date);
		return c.get(Calendar.YEAR);
	}

	public static int getMonthOfDate(Date p_date) {
		Calendar c = Calendar.getInstance();
		c.setTime(p_date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static int getDayOfDate(Date p_date) {
		Calendar c = Calendar.getInstance();
		c.setTime(p_date);
		return c.get(Calendar.DAY_OF_MONTH);
	}
}
