package org.minnie.utility.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static Calendar calendar = Calendar.getInstance();

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 获取当前年份
	 * @return
	 */
	public static int getCurrentYear(){
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月份
	 * @return
	 */
	public static int getCurrentMonth(){
		return calendar.get(Calendar.MONTH )+1;
	}

	/**
	 * 获取当前日(当前月的第几天)
	 * @return
	 */
	public static int getCurrentDayOfMonth(){
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getTime(long currentTimeMillis){
		Date dateTime = new Date(currentTimeMillis);
		return simpleDateFormat.format(dateTime);
	}

}
