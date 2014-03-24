package org.minnie.utility.module.lottery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-24 类说明
 */
public class FiveInElevenApp {
	
	private static Logger logger = Logger.getLogger(FiveInElevenApp.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);
		
		HttpSimulation hs = new HttpSimulation();

		/**
		 * 好运11选5
		 */
		long startTime = System.currentTimeMillis();

		getLuckyFiveInEleven(hs, 2013, 8, 10, "D:/lottery/hljd11/");
		
		long endTime = System.currentTimeMillis();
		
		logger.info("=======================================");
		logger.info("下载数据耗时" + (endTime - startTime) + "ms");

		/**
		 * 老11选5
		 */
//		long startTime = System.currentTimeMillis();
//		
//		getOldFiveInEleven(hs, 2013, 6, 1, "D:/lottery/jxd11/");
//		
//		long endTime = System.currentTimeMillis();
//		
//		logger.info("=======================================");
//		logger.info("下载数据耗时" + (endTime - startTime) + "ms");

		/**
		 * 广东11选5
		 */
//		long startTime = System.currentTimeMillis();
//		
//		getGuangDongFiveInEleven(hs, 2013, 3, 1, "D:/lottery/gdd11/");
//		
//		long endTime = System.currentTimeMillis();
//		
//		logger.info("=======================================");
//		logger.info("下载数据耗时" + (endTime - startTime) + "ms");
		
		
	}
	
	/**
	 * 广东11选5
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getGuangDongFiveInEleven(HttpSimulation hs, Integer beginYear, Integer beginMonth, Integer beginDay, String directory){
		
		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth, beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();
			
			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", "gdd11"));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "84"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps,
					directory + day + ".xls");
			long endTime = System.currentTimeMillis();
			
			logger.info("下载 " +day+ " 数据耗时：" + (endTime - startTime) + "ms");

		}
	}
	
	/**
	 * 老11选5
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getOldFiveInEleven(HttpSimulation hs, Integer beginYear, Integer beginMonth, Integer beginDay, String directory){
		
		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth, beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();
			
			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", "jxd11"));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "78"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps,
					directory + day + ".xls");
			long endTime = System.currentTimeMillis();

			logger.info("下载 " +day+ " 数据耗时" + (endTime - startTime) + "ms");
			
		}
	}
	
	/**
	 * 好运11选5
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getLuckyFiveInEleven(HttpSimulation hs, Integer beginYear, Integer beginMonth, Integer beginDay, String directory){
		
		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth, beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();
			
			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", "hljd11"));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "79"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps,
					directory + day + ".xls");
			long endTime = System.currentTimeMillis();
			
			logger.info("下载 " +day+ " 数据耗时" + (endTime - startTime) + "ms");
			
		}
	}

}
