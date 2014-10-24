package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
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
//		getLeagueList();
//		initTeamList();
		incrementalGetTeamList();
		
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		String link = "http://qiudui.caipiao.163.com/1/0000/661/scpl.html";
//		Set<Long> existIdSet = MysqlDatabseHelper.getExistFootballTeamIdSet(null);
//		try {
//			URL teamUrl = new URL(link);
//			String path = teamUrl.getPath();
//			String resp = hs.getResponseBodyByGet(teamUrl.getHost(), path, nvps);
//			String [] arr = path.split("\\/");
//			FootballTeam team = JsoupHtmlParser.getTeamDetail(resp, Long.valueOf(arr[3]), Long.valueOf(arr[1]), link);
//			List<FootballTeam> list = new ArrayList<FootballTeam>();
//			list.add(team);
//			MysqlDatabseHelper.batchAddFootballTeam(list, existIdSet);
//		} catch (MalformedURLException e) {
//			logger.error(e.getMessage());
//		}
		
		
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
		
		List<FootballLeague> leagueList = JsoupHtmlParser.getLeagueList(response);
		MysqlDatabseHelper.batchAddFootballLeague(leagueList);
	}
	
	/**
	 * 初始化球队信息
	 */
	public static void initTeamList(){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<FootballLeague> leagueList = MysqlDatabseHelper.getFootballLeagueList(null);
		Set<Long> existIdSet = MysqlDatabseHelper.getExistFootballTeamIdSet(null);

		for(FootballLeague league : leagueList){
			String link = league.getLink();
			List<FootballTeam> footballTeamList = new ArrayList<FootballTeam>();

			try {
				URL url = new URL(link);
				String response = hs.getResponseBodyByGet(url.getHost(), url.getFile(), nvps);
				List<String> teamList = JsoupHtmlParser.getTeamList(response);
				
				for(String teamLink : teamList){
					URL teamUrl = new URL(teamLink);
					String path = teamUrl.getPath();
					String resp = hs.getResponseBodyByGet(teamUrl.getHost(), path, nvps);
					String [] arr = path.split("\\/");
					FootballTeam team = JsoupHtmlParser.getTeamDetail(resp, Long.valueOf(arr[3]), Long.valueOf(arr[1]), link);
					footballTeamList.add(team);
				}
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}
			
			MysqlDatabseHelper.batchAddFootballTeam(footballTeamList, existIdSet);
		}
	}
	
	/**
	 * 增量添加球队信息
	 */
	public static void incrementalGetTeamList(){
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<FootballLeague> leagueList = MysqlDatabseHelper.getFootballLeagueList(null);
		Set<Long> existIdSet = MysqlDatabseHelper.getExistFootballTeamIdSet(null);
		
		for(FootballLeague league : leagueList){
			String link = league.getLink();
			List<FootballTeam> footballTeamList = new ArrayList<FootballTeam>();
			
			try {
				URL url = new URL(link);
				String response = hs.getResponseBodyByGet(url.getHost(), url.getFile(), nvps);
				List<String> teamList = JsoupHtmlParser.getTeamList(response);
				
				for(String teamLink : teamList){
					URL teamUrl = new URL(teamLink);
					String path = teamUrl.getPath();
					String resp = hs.getResponseBodyByGet(teamUrl.getHost(), path, nvps);
					String [] arr = path.split("\\/");
					Long teamId = Long.valueOf(arr[3]);
					if(existIdSet.contains(teamId)){
						continue;
					}
					FootballTeam team = JsoupHtmlParser.getTeamDetail(resp, teamId, Long.valueOf(arr[1]), link);
					footballTeamList.add(team);
					existIdSet.add(teamId);
				}
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}
			
			MysqlDatabseHelper.incrementalAddFootballTeam(footballTeamList);
		}
	}
	

}