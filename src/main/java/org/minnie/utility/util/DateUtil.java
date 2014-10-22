package org.minnie.utility.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateUtil {

	// 获取Calendar实例
	private static Calendar calendar = Calendar.getInstance();

	public static SimpleDateFormat standardDateFormat = new SimpleDateFormat(
			Constant.DATE_FORMAT_STANDARD);
	public static SimpleDateFormat neteaselotteryDateFormat = new SimpleDateFormat(
			Constant.DATE_FORMAT_NETEASE_LOTTERY_FIVE_IN_ELEVEN);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			Constant.DATE_FORMAT_WITHOUT_HH_MM_SS);
	private static final Pattern PATTERN_TIMESTAMP = Pattern
			.compile(Constant.TIMESTAMP_FORMAT);

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日(当前月的第几天)
	 * 
	 * @return
	 */
	public static int getCurrentDayOfMonth() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前时间
	 * 
	 * @param currentTimeMillis
	 * @return
	 */
	public static String getTime(long currentTimeMillis) {
		Date dateTime = new Date(currentTimeMillis);
		return standardDateFormat.format(dateTime);
	}

	public static String getDate() {
		return dateFormat.format(new Date());
	}

	/**
	 * 获取yyMMdd格式的日期列表
	 * 
	 * @param beginYear
	 *            初始年份
	 * @param endYear
	 *            结束年份
	 * @return
	 */
	public static List<String> dateTraversal(Integer beginYear, Integer endYear) {
		List<String> list = new ArrayList<String>();

		Calendar cal = Calendar.getInstance(); // 获取Calendar实例
		for (int i = beginYear; i <= endYear; i++) {
			cal.clear();
			cal.set(Calendar.YEAR, i); // 设置年
			for (int j = 0; j < 12; j++) {
				cal.set(Calendar.MONTH, j); // 设置月
				cal.set(Calendar.DAY_OF_MONTH, 1); // 设置月开始第一天日期
				int endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 获得月末日期
				for (int k = 1; k <= endOfMonth; k++) { // 循环打印即可
					// 使用pattern
					list.add(neteaselotteryDateFormat.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} // end of k
			}// end of j
		}// end of i
		return list;
	}

	/**
	 * 获取截止到当前日期的yy格式的日期列表
	 * 
	 * @param beginYear
	 *            初始年份
	 * @return
	 */
	public static List<String> yearTraversal(Integer beginYear) {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat twoBitDateFormat = new SimpleDateFormat("yy");
		Calendar cal = Calendar.getInstance(); // 获取Calendar实例
		int currentYear = getCurrentYear();
		for (int i = beginYear; i <= currentYear; i++) {
			cal.clear();
			cal.set(Calendar.YEAR, i); // 设置年
			list.add(twoBitDateFormat.format(cal.getTime()));
		}// end of i
		return list;
	}

	public static List<String> dateTraversal(Integer beginYear,
			Integer beginMonth, Integer beginDay) {
		List<String> list = new ArrayList<String>();

		Calendar cal = Calendar.getInstance(); // 获取Calendar实例
		int currentYear = getCurrentYear();
		for (int i = beginYear; i <= currentYear; i++) {
			cal.clear();
			cal.set(Calendar.YEAR, i); // 设置年
			// 设置起始月
			int minMonth = 0;
			if (i == beginYear && null != beginMonth && beginMonth >= 1
					&& beginMonth <= 12) {
				minMonth = beginMonth - 1;
			}
			// 设置终止月
			int maxMonth = 12;
			if (i == currentYear) {
				maxMonth = getCurrentMonth();
			}
			for (int j = minMonth; j < maxMonth; j++) {
				cal.set(Calendar.MONTH, j); // 设置月
				int mBeginDay = 1;
				if (i == beginYear && null != beginMonth && j == beginMonth - 1
						&& null != beginDay) {
					mBeginDay = beginDay;
				}
				cal.set(Calendar.DAY_OF_MONTH, mBeginDay); // 设置月开始第一天日期
				int endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 获得月末日期
				if (i == currentYear && j == getCurrentMonth() - 1) {
					endOfMonth = getCurrentDayOfMonth();
				}
				for (int k = mBeginDay; k <= endOfMonth; k++) { // 循环打印即可
					// 使用pattern
					list.add(neteaselotteryDateFormat.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} // end of k
			}// end of j
		}// end of i
		return list;
	}

	public static List<String> standardDateTraversal(Integer beginYear,
			Integer beginMonth, Integer beginDay) {
		List<String> list = new ArrayList<String>();

		Calendar cal = Calendar.getInstance(); // 获取Calendar实例
		int currentYear = getCurrentYear();
		for (int i = beginYear; i <= currentYear; i++) {
			cal.clear();
			cal.set(Calendar.YEAR, i); // 设置年
			// 设置起始月
			int minMonth = 0;
			if (i == beginYear && null != beginMonth && beginMonth >= 1
					&& beginMonth <= 12) {
				minMonth = beginMonth - 1;
			}
			// 设置终止月
			int maxMonth = 12;
			if (i == currentYear) {
				maxMonth = getCurrentMonth();
			}
			for (int j = minMonth; j < maxMonth; j++) {
				cal.set(Calendar.MONTH, j); // 设置月
				int mBeginDay = 1;
				if (i == beginYear && null != beginMonth && j == beginMonth - 1
						&& null != beginDay) {
					mBeginDay = beginDay;
				}
				cal.set(Calendar.DAY_OF_MONTH, mBeginDay); // 设置月开始第一天日期
				int endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 获得月末日期
				if (i == currentYear && j == getCurrentMonth() - 1) {
					endOfMonth = getCurrentDayOfMonth();
				}
				for (int k = mBeginDay; k <= endOfMonth; k++) { // 循环打印即可
					// 使用pattern
					list.add(dateFormat.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} // end of k
			}// end of j
		}// end of i
		return list;
	}

	public static String dateCovert(String sourceDateString) {

		return dateCovert(sourceDateString, neteaselotteryDateFormat,
				dateFormat);
	}

	public static String dateCovert(String sourceDateString,
			SimpleDateFormat sourDateFormat, SimpleDateFormat targetDateFormat) {

		String sourceDateStringDateString = null;
		try {
			Date date = sourDateFormat.parse(sourceDateString);
			sourceDateStringDateString = targetDateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sourceDateStringDateString;
	}

	// Convert Unix timestamp to normal date style
	public static Date TimeStamp2Date(String timestampString) {

		if(null != timestampString){
			Matcher matcher = PATTERN_TIMESTAMP.matcher(timestampString);
			if (matcher.find()) {
				Long timestamp = Long.parseLong(matcher.group());
				return new Timestamp(timestamp);
			}
		}
		return null;

	}
}
