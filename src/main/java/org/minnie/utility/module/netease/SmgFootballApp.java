package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;

/**
 * 竞彩足球
 * 
 * url  http://caipiao.163.com/order/jczq-hunhe/
 * 
 * @author eazytec
 * 
 */
public class SmgFootballApp {
	
	private static Logger logger = Logger.getLogger(SmgFootballApp.class.getName());

	private static HttpSimulation hs = new HttpSimulation();

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	private static final int THREAD_POOL_SIZE = 20;
	
	public static void main(String[] args) {
		
		// 获取加载系统配置时间
		long configurationStartTime = System.currentTimeMillis();

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);
		
//		init();
		getLeagueList();
	}
	
	public static void init(){
		
//		http://caipiao.163.com/order/jczq-hunhe/?betDate=2014-10-07
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("betDate", "2014-10-07"));
		
		String response = hs.getResponseBodyByGet("caipiao.163.com", "/order/jczq-hunhe/", nvps);
		
		JsoupHtmlParser.getMatchList(response);
	}
	
	public static void getLeagueList(){
//		http://saishi.caipiao.163.com/
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("betDate", "2014-10-07"));
		
		String response = hs.getResponseBodyByGet("saishi.caipiao.163.com", "/", nvps);
		
		JsoupHtmlParser.getLeagueList(response);
	}
	

}