package org.minnie.utility.util;

import java.util.Calendar;

public class DateUtil {
	
	private static Calendar calendar = Calendar.getInstance();
	
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

}
