package org.minnie.utility.module.lottery;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.entity.lottery.SuperLotto;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-24
 * 类说明
 */
public class SuperLottoApp {

	private static Logger logger = Logger.getLogger(SuperLottoApp.class.getName());

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
		
		List<SuperLotto> list = getSuperLotto(hs, 2007);
		MysqlDatabseHelper.batchAddLotterySuperLotto(list);
		
	}
	
	/**
	 * 获取大乐透数据
	 * @param hs
	 * @param beginYear
	 * @return
	 */
	public static List<SuperLotto> getSuperLotto(HttpSimulation hs, Integer beginYear){
		
		List<SuperLotto> list = new ArrayList<SuperLotto>();	
		
		List<String> yearList = DateUtil.yearTraversal(beginYear);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String year : yearList) {
			long startTime = System.currentTimeMillis();
			
			nvps.clear();
			nvps.add(new BasicNameValuePair("startPhase", year + "001"));
			nvps.add(new BasicNameValuePair("endPhase", year + "153"));
			nvps.add(new BasicNameValuePair("phaseOrder", "up"));
			nvps.add(new BasicNameValuePair("onlyBody", "true"));
			
			String responseBody = hs.getResponseBodyByGet("trend.sohu.lecai.com", "/dlt/redBaseTrend.action", nvps);
			Integer iYear = 2000 + Integer.valueOf(year);
			list.addAll(JsoupHtmlParser.getSohuLotto(responseBody, "#chartTable", iYear));
			
			long endTime = System.currentTimeMillis();
			
			logger.info("下载 " +iYear+ "年 数据耗时：" + (endTime - startTime) + "ms");

		}
		return list;
	}

}
