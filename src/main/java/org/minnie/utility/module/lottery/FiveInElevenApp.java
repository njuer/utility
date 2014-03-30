package org.minnie.utility.module.lottery;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.entity.lottery.FiveInEleven;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.ExcelUtil;
import org.minnie.utility.util.FileUtil;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-24 类说明
 */
public class FiveInElevenApp {

	private static Logger logger = Logger.getLogger(FiveInElevenApp.class
			.getName());

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

		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(ExcelUtil.readDataFrom("D:/lottery/gdd11/130301.xls",
		// 0, "gdd11"));

		HttpSimulation hs = new HttpSimulation();
		//
		// /**
		// * 好运11选5
		// */
		// long startTime = System.currentTimeMillis();
		//
		// getLuckyFiveInEleven(hs, 2013, 8, 10, "D:/lottery/hljd11/");
		//
		// long endTime = System.currentTimeMillis();
		//
		// logger.info("=======================================");
		// logger.info("下载数据耗时" + (endTime - startTime) + "ms");

		// /**
		// * 老11选5
		// */
		// long startTime = System.currentTimeMillis();
		//
		// getOldFiveInEleven(hs, 2013, 6, 1, "D:/lottery/jxd11/");
		//
		// long endTime = System.currentTimeMillis();
		//
		// logger.info("=======================================");
		// logger.info("下载数据耗时" + (endTime - startTime) + "ms");

		// /**
		// * 广东11选5
		// */
		// long startTime = System.currentTimeMillis();
		//
		// getGuangDongFiveInEleven(hs, 2013, 3, 1, "D:/lottery/gdd11/");
		//
		// long endTime = System.currentTimeMillis();
		//
		// logger.info("=======================================");
		// logger.info("下载数据耗时" + (endTime - startTime) + "ms");

		// /**
		// * 广东11选5
		// */
		// List<FiveInEleven> list = new ArrayList<FiveInEleven>();
		// List<String> fileList = FileUtil.getDirectory(new
		// File("D:/lottery/gdd11"));
		// for(String path : fileList){
		// list.addAll(ExcelUtil.readDataFrom(path, 0, "gdd11"));
		// }
		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(list);

		// /**
		// * 好运11选5
		// */
		// list.clear();
		// fileList.clear();
		// fileList = FileUtil.getDirectory(new File("D:/lottery/hljd11"));
		// for(String path : fileList){
		// list.addAll(ExcelUtil.readDataFrom(path, 0, Constant.LOTTERY_FIVE_IN_ELEVEN_LUCKY));
		// }
		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(list);

		// /**
		// * 老11选5
		// */
		// list.clear();
		// fileList.clear();
		// fileList = FileUtil.getDirectory(new File("D:/lottery/jxd11"));
		// for(String path : fileList){
		// list.addAll(ExcelUtil.readDataFrom(path, 0, Constant.LOTTERY_FIVE_IN_ELEVEN_OLD));
		// }
		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(list);

		// getGDLotteryFiveInEleven(hs, null);

		/**
		 * 11选5数据初始化[来源：广东体彩网]
		 */
		downloadAllGDLotteryFiveInEleven(hs);
//		getGDLotteryFiveInEleven(hs, "2009-11-12");
		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(list, null);

		// getDaysToUpdate(14032429);

		/**
		 * 11选5数据更新[来源：网易彩票excel]
		 */
		// Map<String, Integer> map = MysqlDatabseHelper.statsMaxPeriod(null);
		// upgradeAllFiveInEleven(hs, map, "D:/lottery");
	}

	/**
	 * 广东11选5
	 * 
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getGuangDongFiveInEleven(HttpSimulation hs,
			Integer beginYear, Integer beginMonth, Integer beginDay,
			String directory) {

		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth,
				beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();

			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", "gdd11"));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "84"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps, directory
							+ day + ".xls");
			long endTime = System.currentTimeMillis();

			logger.info("下载 " + day + " 数据耗时：" + (endTime - startTime) + "ms");

		}
	}

	/**
	 * 老11选5
	 * 
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getOldFiveInEleven(HttpSimulation hs, Integer beginYear,
			Integer beginMonth, Integer beginDay, String directory) {

		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth,
				beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();

			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", Constant.LOTTERY_FIVE_IN_ELEVEN_OLD));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "78"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps, directory
							+ day + ".xls");
			long endTime = System.currentTimeMillis();

			logger.info("下载 " + day + " 数据耗时" + (endTime - startTime) + "ms");

		}
	}

	/**
	 * 好运11选5
	 * 
	 * @param hs
	 * @param beginYear
	 * @param beginMonth
	 * @param beginDay
	 * @param directory
	 */
	public static void getLuckyFiveInEleven(HttpSimulation hs,
			Integer beginYear, Integer beginMonth, Integer beginDay,
			String directory) {

		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		List<String> dayList = DateUtil.dateTraversal(beginYear, beginMonth,
				beginDay);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();

			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", Constant.LOTTERY_FIVE_IN_ELEVEN_LUCKY));
			nvps.add(new BasicNameValuePair("beginPeriod", day + "01"));
			nvps.add(new BasicNameValuePair("endPeriod", day + "79"));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps, directory
							+ day + ".xls");
			long endTime = System.currentTimeMillis();

			logger.info("下载 " + day + " 数据耗时" + (endTime - startTime) + "ms");

		}
	}

	public static void getGDLotteryFiveInEleven(HttpSimulation hs, String date) {
		// http://www.gdlottery.cn/zst11xuan5.jspx?method=to11x5kjggzst&date=2014-03-24
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		long startTime = System.currentTimeMillis();

		nvps.clear();
		nvps.add(new BasicNameValuePair("method", "to11x5kjggzst"));
//		nvps.add(new BasicNameValuePair("date", "2014-03-24"));
		nvps.add(new BasicNameValuePair("date", date));

		String response = hs.getResponseBodyByGet("www.gdlottery.cn",
				"/zst11xuan5.jspx", nvps);

		// JsoupHtmlParser.getGDLotteryFiveInElevenDateList(response,"#date",null);
		JsoupHtmlParser.getGDLotteryFiveInEleven(response, "table", date);
		long endTime = System.currentTimeMillis();

		logger.info("下载数据耗时：" + (endTime - startTime) + "ms");
	}

	public static List<FiveInEleven> getAllGDLotteryFiveInEleven(
			HttpSimulation hs) {

		long getAllStartTime = System.currentTimeMillis();

		List<FiveInEleven> list = new ArrayList<FiveInEleven>();

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("method", "to11x5kjggzst"));
		nvps.add(new BasicNameValuePair("date", "2009-11-11"));

		String sql = "insert into lottery_five_in_eleven_gd (period, red_1, red_2, red_3, red_4, red_5, category, period_date) VALUES (?,?,?,?,?,?,?,?)";

		List<String> dateList = JsoupHtmlParser
				.getGDLotteryFiveInElevenDateList(hs.getResponseBodyByGet(
						"www.gdlottery.cn", "/zst11xuan5.jspx", nvps), "#date",
						null);

		for (String date : dateList) {
			long startTime = System.currentTimeMillis();

			nvps.clear();
			nvps.add(new BasicNameValuePair("method", "to11x5kjggzst"));
			nvps.add(new BasicNameValuePair("date", date));

			String response = hs.getResponseBodyByGet("www.gdlottery.cn",
					"/zst11xuan5.jspx", nvps);

			list.addAll(JsoupHtmlParser.getGDLotteryFiveInEleven(response,
					"table", date));

			long endTime = System.currentTimeMillis();
			logger.info("下载" + date + "期 数据耗时：" + (endTime - startTime) + "ms");
		}

		long getAllEndTime = System.currentTimeMillis();
		logger.info("下载数据总耗时：" + (getAllEndTime - getAllStartTime) + "ms");

		return list;
	}

	public static void downloadAllGDLotteryFiveInEleven(HttpSimulation hs) {

		long getAllStartTime = System.currentTimeMillis();

		List<FiveInEleven> list = new ArrayList<FiveInEleven>();

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("method", "to11x5kjggzst"));
		nvps.add(new BasicNameValuePair("date", "2009-11-11"));

		String sql = "insert into lottery_five_in_eleven (period, red_1, red_2, red_3, red_4, red_5, lottery_number, category, period_date, create_date, update_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		List<String> dateList = JsoupHtmlParser
				.getGDLotteryFiveInElevenDateList(hs.getResponseBodyByGet(
						"www.gdlottery.cn", "/zst11xuan5.jspx", nvps), "#date",
						null);

		Set<String> daySet = MysqlDatabseHelper.statsPeriodPerDay(null);
		
		
		for (String date : dateList) {
			long startTime = System.currentTimeMillis();
			if(daySet.contains(date)){
				continue;
			}
			
			list.clear();

			nvps.clear();
			nvps.add(new BasicNameValuePair("method", "to11x5kjggzst"));
			nvps.add(new BasicNameValuePair("date", date));

			String response = hs.getResponseBodyByGet("www.gdlottery.cn",
					"/zst11xuan5.jspx", nvps);

			list = JsoupHtmlParser.getGDLotteryFiveInEleven(response, "table",
					date);
			MysqlDatabseHelper.batchAddLotteryFiveInEleven(list, sql);

			long endTime = System.currentTimeMillis();
			logger.info("下载" + date + "期 数据耗时：" + (endTime - startTime) + "ms");
		}

		long getAllEndTime = System.currentTimeMillis();
		logger.info("下载数据总耗时：" + (getAllEndTime - getAllStartTime) + "ms");

	}

	/**
	 * 下载某类11选5更新文件
	 * 
	 * @param hs
	 * @param category
	 * @param maxPeriod
	 * @param directory
	 */
	public static void downFiveInElevenPatch(HttpSimulation hs,
			String category, Integer maxPeriod, String directory) {

		// 如果目录不存在，则创建目录
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String sPeriod = String.valueOf(maxPeriod);

		Integer mYear = 2000 + Integer.valueOf(sPeriod.substring(0, 2));
		Integer mMonth = Integer.valueOf(sPeriod.substring(2, 4));
		Integer mDay = Integer.valueOf(sPeriod.substring(4, 6));
		Integer mPeriod = Integer.valueOf(sPeriod.substring(6, 8));

		Integer periods = 84;// 广东11选5，默认最大84
		if (Constant.LOTTERY_FIVE_IN_ELEVEN_LUCKY.equals(category)) {
			periods = 79;// 好运11选5
		} else if (Constant.LOTTERY_FIVE_IN_ELEVEN_OLD.equals(category)) {
			periods = 78;// 旧11选5
		}

		List<String> dayList = DateUtil.dateTraversal(mYear, mMonth, mDay);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String day : dayList) {
			long startTime = System.currentTimeMillis();

			String beginPeriod = day + "01";
			String endPeriod = day + periods;
			if (sPeriod.substring(0, 6).equals(day)) {
				if (mPeriod >= periods) {
					continue;
				}
				beginPeriod = StringUtil.getTwoBitValue(maxPeriod + 1);
			}

			nvps.clear();
			nvps.add(new BasicNameValuePair("gameEn", category));
			nvps.add(new BasicNameValuePair("beginPeriod", beginPeriod));
			nvps.add(new BasicNameValuePair("endPeriod", endPeriod));
			hs.getFileByDefaultSchemePort("zx.caipiao.163.com",
					"/trend/downloadTrendAwardNumber.html", nvps, directory
							+ day + ".xls");
			long endTime = System.currentTimeMillis();

			logger.info("下载 " + day + "[" + category + "] 数据耗时"
					+ (endTime - startTime) + "ms");

		}
	}

	/**
	 * 下载所有11选5更新文件
	 * 
	 * @param hs
	 * @param map
	 * @param directory
	 */
	public static void upgradeAllFiveInEleven(HttpSimulation hs,
			Map<String, Integer> map, String directory) {

		FileUtil.deleteFile(directory);

		List<FiveInEleven> list = new ArrayList<FiveInEleven>();

		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> entry = it.next();
			String category = entry.getKey();
			Integer period = entry.getValue();
			directory += File.separator + category + File.separator;

			downFiveInElevenPatch(hs, category, period, directory);

			List<String> fileList = FileUtil.getDirectory(new File(directory));
			for (String path : fileList) {
				list.addAll(ExcelUtil.readDataFrom(path, 0, category));
			}
		}

		MysqlDatabseHelper.batchAddLotteryFiveInEleven(list);

	}
	
	/**
	 * 11选5分析
	 * @param category	类别：广东11选5、好运11选5、旧11选5
	 * @param date	分析日期
	 */
	public static void fiveInElevenAnaylse(String category, String date) {

		List<FiveInEleven> list = MysqlDatabseHelper.getFiveInElevenList(category,date);
		
		List<FiveInEleven> adjacentList = new ArrayList<FiveInEleven>();
		List<FiveInEleven> consecutiveList = new ArrayList<FiveInEleven>();
		Map<Integer, FiveInEleven> map = new HashMap<Integer, FiveInEleven>();
		Set<Integer> set = new HashSet<Integer>();

		FiveInEleven last = null;
		List<String> result = new ArrayList<String>(5); 
		List<String> temp = new ArrayList<String>(5); 
		int size = list.size();
		for(int p = 0; p < size; p++){
			FiveInEleven fie = list.get(p);
			Integer period = fie.getPeriod();
			map.put(period, fie);
			
			temp.clear();
			result.clear();
//			set.clear();
			
			List<String> balls = fie.getRed();
			
			int [] red = new int[12];
			for(String ball : balls){
				red[Integer.valueOf(ball)] = 1;
			}
			
			for(int i = 1; i < 12; i++){
				if(red[i] == 1){
					result.add(StringUtil.getTwoBitValue(i));
				} else {
					if(result.size() > 2){
						break;
					} else {
						result.clear();
					}
				}
			} // end of for(int i = 1; i < 12; i++)
			
			/**
			 * 处理与上期相同
			 */
			if(null != last){
				List<String> lastBalls = last.getRed();
				int [] lastRed = new int[12];
				for(String ball : lastBalls){
					lastRed[Integer.valueOf(ball)] = 1;
				}
				if(isTheSame(lastRed, red)){
					adjacentList.add(last);
					adjacentList.add(fie);
				}
			}
			
			/**
			 * 处理连号
			 */
			if(result.size() > 2){
				fie.setConsecutive(result.toString());
				fie.setAmount(result.size());
				consecutiveList.add(fie);
			}
			
			/**
			 * 处理同号
			 */
			for(int q = 0; q < p; q++){
				FiveInEleven prev = list.get(q);
				List<String> prevRed = prev.getRed();
				int [] prevRedArray = new int[12];
				for(String ball : prevRed){
					prevRedArray[Integer.valueOf(ball)] = 1;
				}
				
				if(isTheSame(prevRedArray,red)){
					set.add(prev.getPeriod());
					set.add(period);
				}
			}
			
			last = fie;
		}
		
		//删除某日[连号]分析数据
		MysqlDatabseHelper.deleteFiveInElevenAnalysisByConsecutive(date);
		//保存[连号]
		if(consecutiveList.size() > 0){
			MysqlDatabseHelper.batchAddFiveInElevenConsecutive(consecutiveList, category, date, null);
		}

		//删除某日[与上期相同]分析数据
		MysqlDatabseHelper.deleteFiveInElevenAnalysisByAdjacent(date);
		//保存[与上期相同]
		if(adjacentList.size() > 0){
			MysqlDatabseHelper.batchAddFiveInElevenAdjacent(adjacentList, category, date, null);
		}
		
		//删除某日[同号]分析数据
		MysqlDatabseHelper.deleteFiveInElevenAnalysisBySame(date);
		//保存[同号]
		List<Integer> samePeriodList= new ArrayList<Integer>(set);
		Collections.sort(samePeriodList);
		List<FiveInEleven> sameList= new ArrayList<FiveInEleven>();
		for(Integer period:samePeriodList){
			sameList.add(map.get(period));
		}
		if(samePeriodList.size() > 0){
			MysqlDatabseHelper.batchAddFiveInElevenSame(sameList, category, date, null);
		}
		
	}
	
	public static void fiveInElevenAnaylse(String category){
		//使用当前日期
		fiveInElevenAnaylse(category,DateUtil.getDate());
	}
	
	/**
	 * 比较两个数组值是否相等[跳过下标0]
	 * @param last
	 * @param current
	 * @return
	 */
	public static boolean isTheSame(int [] last, int [] current){
		boolean result = true;
		for(int i = 1; i < 12; i++){
			if(last[i] != current[i]){
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static void analyseSame(List<FiveInEleven> list, int index){
		
	}
	
	/**
	 * 两个FiveInEleven实例，验证相同红球个数是否满足[minHit,maxHit]区间
	 * @param target
	 * @param candidate
	 * @return
	 */
	public static boolean analyseCandidate(FiveInEleven target, FiveInEleven candidate, int minHit, int maxHit){
		
//		List<String> targetList = target.getRed();
		List<String> targetList = new ArrayList<String>();
		List<String> candidateList = candidate.getRed();
		
		targetList.addAll(target.getRed());
		//求交集
		targetList.retainAll(candidateList);
		
		int size = targetList.size();
		
		if(size >= minHit && size <= maxHit){
			return true;
		}
		
		return false;
	}

	public static int analyseCandidateHit(FiveInEleven target, FiveInEleven candidate, int minHit, int maxHit){
		
		List<String> targetList = new ArrayList<String>();
		List<String> candidateList = candidate.getRed();
		
		targetList.addAll(target.getRed());
		//求交集
		targetList.retainAll(candidateList);
		
		int size = targetList.size();
		
		if(size >= minHit && size <= maxHit){
			return size;
		}
		
		return -1;
	}

}