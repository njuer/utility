package org.minnie.utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.module.lottery.FiveInElevenApp;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.ImageUtil;

public class Test {
	public static Pattern urlPattern = Pattern
			.compile(Constant.REG_DOUBLE_COLOR_RED_BALL);

	private static Logger logger = Logger.getLogger(Test.class.getName());

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

	    URL hp;
		try {
			hp = new URL("http://qiudui.caipiao.163.com/1/7243/1519/scpl.html");
		    System.out.println("Protocol: " + hp.getProtocol());
		    System.out.println("Port: " + hp.getPort());
		    System.out.println("Host: " + hp.getHost());
		    System.out.println("File: " + hp.getFile());
		    String path = hp.getPath();
		    System.out.println("Path: " + path);
		    System.out.println("Ext:" + hp.toExternalForm());
		    
		    String [] arr = path.split("\\/");
		    System.out.println(Arrays.toString(arr));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		String leagueLogoLink = "http://pimg1.126.net/caipiao_info/images/league/70/645.png?201410230305";
//		int symbolQuestion = leagueLogoLink.indexOf("?");
//		if(symbolQuestion >= 0){
//			leagueLogoLink = leagueLogoLink.substring(0, symbolQuestion);
//		}
//		ImageUtil.save2File("D:\\", "league", leagueLogoLink);
//		logger.info(Integer.valueOf("02"));
		// String ss =
		// "09,11,17,18,22,3301,10,11,15,19,2803,06,11,21,22,27<br />02,03,10,15,17,2704,08,17,18,27,3202,07,10,12,14,25<br />04,06,16,22,23,3104,10,21,23,27,3007,16,18,20,26,33<br />02,06,11,23,31,3309,13,19,23,26,3206,13,14,15,26,33<br />03,16,17,22,24,2508,10,18,21,31,3207,12,13,19,20,21<br /><br />03,16,21,25,29,3203,10,21,30,31,3302,05,20,23,25,27<br />09,11,15,18,21,3001,04,06,08,24,3307,13,23,24,25,32<br />05,11,14,22,24,3006,07,11,15,19,3201,02,07,08,12,33<br />03,08,20,23,26,2705,12,17,20,28,3117,18,20,24,25,33<br />03,06,14,24,28,2901,09,11,24,28,2902,06,09,22,26,29<br /><br />04,07,14,28,29,3014,19,21,22,24,2601,02,08,23,26,30<br />04,11,27,28,30,3302,09,11,18,25,3001,02,20,21,23,31<br />04,15,21,29,30,3303,08,20,28,29,3205,10,13,28,29,30<br />06,08,11,20,27,2904,16,18,23,29,3103,04,11,27,32,33<br />04,15,21,28,29,3111,14,21,22,24,2901,06,14,17,18,19<br /><br />04,05,06,07,21,2404,17,22,28,30,3104,10,21,22,25,32<br />02,05,10,14,28,3101,16,21,25,26,3102,04,13,19,26,31<br />02,05,06,18,26,2703,17,18,20,28,3304,20,22,27,30,31<br />01,07,14,17,24,2509,17,20,21,24,3008,12,23,27,28,31<br />03,05,06,19,20,3104,06,07,09,13,3206,10,16,19,27,29<br /><br />11,16,20,22,24,2705,06,11,19,28,3104,07,17,18,20,26<br />02,18,19,21,22,3204,08,19,24,25,3104,05,09,16,22,26<br />01,02,09,26,27,3305,13,16,20,26,3102,06,08,11,25,26<br />03,05,06,09,22,31";
		//
		// Matcher matcher = urlPattern.matcher(ss);
		// int size = 0;
		// while (matcher.find()) {
		// size++;
		// System.out.println(matcher.group());
		// }
		// System.out.println(size);
		// HttpSimulation hs = new HttpSimulation();
		// hs.get("d:/0431la.xls");
		
//		String category = Constant.LOTTERY_FIVE_IN_ELEVEN_GUANGDONG;
////		String date = "2010-01-01";
//		
//		List<String> list = DateUtil.standardDateTraversal(2014, 3, 1);
//		for(String date : list){
//			FiveInElevenApp.fiveInElevenAnaylse(category,date);
//		}
//		System.out.println(DateUtil.getDate());
			
//		List<String> list = new ArrayList<String>(5); 
//		list.add("01");
//		list.add("03");
//		list.add("04");
//		list.add("05");
//		list.add("11");
//		
//		List<String> result = new ArrayList<String>(5); 
//		
//		int [] arr = new int[12];
//		for(String ball : list){
//			arr[Integer.valueOf(ball)] = 1;
//		}
//		
//		for(int i = 1; i < 12; i++){
//			if(arr[i] == 1){
//				result.add(StringUtil.getTwoBitValue(i));
//			} else {
//				if(result.size() > 2){
//					break;
//				} else {
//					result.clear();
//				}
//			}
//		}
//
//		System.out.println(Arrays.toString(arr));
//		
//		
//		if(result.size() > 2){
//			System.out.println(result);
//		} else {
//			result.clear();
//		}
		
		
//		FileUtil.getDirectory(new File("D:/lottery/gdd11"));
		
//		MysqlDatabseHelper.batchAddLotteryFiveInEleven(ExcelUtil.readDataFrom("D:/lottery/gdd11/130301.xls", 0, "gdd11"));
		
//		showDate(2011, 6);
//		List<String> list = DateUtil.dateTraversal(2010, 2014);
		
//		List<String> list = DateUtil.dateTraversal(2014, 2, 14);
//		for(String d : list){
//			System.out.println(d);
//		}
		
		
//		http://trend.sohu.lecai.com/dlt/redBaseTrend.action?startPhase=07001&endPhase=07200&phaseOrder=up&coldHotOrder=number&onlyBody=false
//		HttpSimulation hs = new HttpSimulation();
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		
//		nvps.add(new BasicNameValuePair("startPhase", "14001"));
//		nvps.add(new BasicNameValuePair("endPhase", "14201"));
//		nvps.add(new BasicNameValuePair("phaseOrder", "up"));
//		nvps.add(new BasicNameValuePair("onlyBody", "true"));
//		
//		String res = hs.getResponseBodyByGet("trend.sohu.lecai.com", "/dlt/redBaseTrend.action", nvps);
//		System.out.println(res);
//		JsoupHtmlParser.getSohuLotto(res, "#chartTable");
	}

	public static void showDate(int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		Calendar calendar = Calendar.getInstance(); // 获取Calendar实例
		calendar.set(Calendar.YEAR, year); // 设置年
		calendar.set(Calendar.MONTH, month - 1); // 设置月
		calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置月开始第一天日期
		int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 获得月末日期
		for (int i = 1; i <= end; i++) { // 循环打印即可
		// System.out.printf("<a href=xxx>%tF</a>\n", calendar.getTime());
			// 使用pattern
			System.out.println(format.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

}
