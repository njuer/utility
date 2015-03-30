package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.module.netease.dao.FootballMatchDao;
import org.minnie.utility.module.netease.entity.FootballMatch;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.ExcelUtil;
import org.minnie.utility.util.FileUtil;
import org.minnie.utility.util.SystemServiceUtil;

/**
 * 竞彩足球
 * 
 * url http://caipiao.163.com/order/jczq-hunhe/
 * 
 * @author eazytec
 * 
 */
public class SmgFootballApp {

	private static Logger logger = Logger.getLogger(SmgFootballApp.class
			.getName());

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

		// init();
		// getLeagueList();
		// initTeamList();
		// incrementalGetTeamList();

		 SystemServiceUtil.startMysql();
		
		 String dir = "F:" + File.separator + "temp" + File.separator +
		 "lottery" + File.separator + "match";
		 
//		 initMatchResult(3,dir);
		 persistMatch(1,dir);
//		 updateMatchResult("2014-12-06");
		
		 SystemServiceUtil.stopMysql();

//		getMatchResult(DateUtil.getDate(), DateUtil.getDate(), null, null);
	}

	public static void init() {

		// http://caipiao.163.com/order/jczq-hunhe/?betDate=2014-10-07

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// nvps.add(new BasicNameValuePair("betDate", "2014-10-28"));

		String response = hs.getResponseBodyByGet("caipiao.163.com",
				"/order/jczq-hunhe/", nvps);

		JsoupHtmlParser.getMatchList(response);
	}

	public static void getLeagueList() {
		// http://saishi.caipiao.163.com/
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// nvps.add(new BasicNameValuePair("betDate", "2014-10-07"));

		String response = hs.getResponseBodyByGet("saishi.caipiao.163.com",
				"/", nvps);

		List<FootballLeague> leagueList = JsoupHtmlParser
				.getLeagueList(response);
		MysqlDatabseHelper.batchAddFootballLeague(leagueList);
	}

	/**
	 * 初始化球队信息
	 */
	public static void initTeamList() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<FootballLeague> leagueList = MysqlDatabseHelper
				.getFootballLeagueList(null);
		Set<Long> existIdSet = MysqlDatabseHelper
				.getExistFootballTeamIdSet(null);

		for (FootballLeague league : leagueList) {
			String link = league.getLink();
			List<FootballTeam> footballTeamList = new ArrayList<FootballTeam>();

			try {
				URL url = new URL(link);
				String response = hs.getResponseBodyByGet(url.getHost(),
						url.getFile(), nvps);
				List<String> teamList = JsoupHtmlParser.getTeamList(response);

				for (String teamLink : teamList) {
					URL teamUrl = new URL(teamLink);
					String path = teamUrl.getPath();
					String resp = hs.getResponseBodyByGet(teamUrl.getHost(),
							path, nvps);
					String[] arr = path.split("\\/");
					FootballTeam team = JsoupHtmlParser.getTeamDetail(resp,
							Long.valueOf(arr[3]), Long.valueOf(arr[1]), link);
					footballTeamList.add(team);
				}
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}

			MysqlDatabseHelper.batchAddFootballTeam(footballTeamList,
					existIdSet);
		}
	}

	/**
	 * 增量添加球队信息
	 */
	public static void incrementalGetTeamList() {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<FootballLeague> leagueList = MysqlDatabseHelper
				.getFootballLeagueList(null);
		Set<Long> existIdSet = MysqlDatabseHelper
				.getExistFootballTeamIdSet(null);

		for (FootballLeague league : leagueList) {
			String link = league.getLink();
			List<FootballTeam> footballTeamList = new ArrayList<FootballTeam>();

			try {
				URL url = new URL(link);
				String response = hs.getResponseBodyByGet(url.getHost(),
						url.getFile(), nvps);
				List<String> teamList = JsoupHtmlParser.getTeamList(response);

				for (String teamLink : teamList) {
					URL teamUrl = new URL(teamLink);
					String path = teamUrl.getPath();
					String resp = hs.getResponseBodyByGet(teamUrl.getHost(),
							path, nvps);
					String[] arr = path.split("\\/");
					Long teamId = Long.valueOf(arr[3]);
					if (existIdSet.contains(teamId)) {
						continue;
					}
					FootballTeam team = JsoupHtmlParser.getTeamDetail(resp,
							teamId, Long.valueOf(arr[1]), link);
					footballTeamList.add(team);
					existIdSet.add(teamId);
				}
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}

			MysqlDatabseHelper.incrementalAddFootballTeam(footballTeamList);
		}
	}

	/**
	 * 生成每日赛事(结果)Excel
	 * 
	 * @param BacktrackingDays
	 *            回溯天数
	 * @param dir
	 *            文件存放目录
	 */
	public static void generateMatch(int BacktrackingDays, String dir) {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<String> dateList = DateUtil.getLastDate(BacktrackingDays);
		int size = dateList.size();
		for (int i = 0; i < size; i++) {
			String date = dateList.get(i);
			nvps.clear();
			if (i > 0) {
				nvps.add(new BasicNameValuePair("betDate", date));
			}
			String response = hs.getResponseBodyByGet("caipiao.163.com",
					"/order/jczq-hunhe/", nvps);
			List<SmgFootball> matchList = JsoupHtmlParser
					.getMatchList(response);
			ExcelUtil.generateDailyMatch(matchList, dir, date);
		}
	}

	public static void persistMatch(int BacktrackingDays, String dir) {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<String> dateList = DateUtil.getLastDate(BacktrackingDays);
		Set<Long> existIdSet = FootballMatchDao
				.getExistFootballMatchIdSet(null);
		int size = dateList.size();
		for (int i = 0; i < size; i++) {
			String date = dateList.get(i);
			nvps.clear();
			if (i > 0) {
				nvps.add(new BasicNameValuePair("betDate", date));
			}
			String response = hs.getResponseBodyByGet("caipiao.163.com",
					"/order/jczq-hunhe/", nvps);
			List<FootballMatch> matchList = JsoupHtmlParser
					.getFootballMatchList(response);
			
			if(matchList.size() > 0){
				FootballMatchDao.batchAddFootballMatch(matchList, existIdSet);

				ExcelUtil.generateDailyFootballMatch(matchList, dir, date);
				
				FileUtil.copyFile(dir + File.separator  + date + ".xlsx", dir + File.separator  + ".." + File.separator + "analysis"+ File.separator  +date + "-analysis.xlsx");
			}
		}
	}

	/**
	 * 获取彩票365网站比赛结果
	 * 
	 * @param startTime
	 *            开始日期
	 * @param endTime
	 *            结束日期
	 * @param leagueNameCn
	 *            联盟(中文)名称
	 * @param matchList
	 *            赛事列表
	 */
	public static void getMatchResult(String startTime, String endTime,
			String leagueNameCn, List<FootballMatch> matchList) {
		// http://cp.caipiao365.com/dc/getKaijiangFootBall.action?startTime=2014-11-01&endTime=2014-11-02&league=%E8%91%A1%E8%90%84%E7%89%99%E8%B6%85%E7%BA%A7%E8%81%94%E8%B5%9B

		List<FootballMatch> matchResultList = new ArrayList<FootballMatch>();

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (StringUtils.isNotBlank(startTime)) {
			nvps.add(new BasicNameValuePair("startTime", startTime));
		}
		if (StringUtils.isNotBlank(endTime)) {
			nvps.add(new BasicNameValuePair("endTime", endTime));
		}
		if (StringUtils.isNotBlank(leagueNameCn)) {
			try {
				nvps.add(new BasicNameValuePair("league", URLEncoder.encode(
						leagueNameCn, "utf-8")));
			} catch (UnsupportedEncodingException e) {
				logger.error("联盟名称转码失败：" + e.getMessage());
			}
		}

		String response = hs.getResponseBodyByGet("cp.caipiao365.com",
				"/dc/getKaijiangFootBall.action", nvps);
		matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(response,
				matchList));

		int pages = JsoupHtmlParser.getTotalPagesByCaipiao365(response);
		for (int i = 1; i < pages; i++) {
			nvps.clear();
			if (StringUtils.isNotBlank(startTime)) {
				nvps.add(new BasicNameValuePair("startTime", startTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				nvps.add(new BasicNameValuePair("endTime", endTime));
			}
			if (StringUtils.isNotBlank(leagueNameCn)) {
				try {
					nvps.add(new BasicNameValuePair("league", URLEncoder
							.encode(leagueNameCn, "utf-8")));
				} catch (UnsupportedEncodingException e) {
					logger.error("联盟名称转码失败：" + e.getMessage());
				}
			}
			nvps.add(new BasicNameValuePair("jumpPage", String.valueOf(i + 1)));
			response = hs.getResponseBodyByGet("cp.caipiao365.com",
					"/dc/getKaijiangFootBall.action", nvps);
			matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(
					response, matchList));
		}
	}
	
	public static void initMatchResult(int BacktrackingDays, String dir) {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<String> backtrackingDayList = DateUtil.getLastDate(BacktrackingDays);
		Set<Long> existIdSet = FootballMatchDao.getExistFootballMatchIdSet(null);
		int size = backtrackingDayList.size();
		for (int i = 0; i < size; i++) {
			String date = backtrackingDayList.get(i);
			nvps.clear();
			if (i > 0) {
				nvps.add(new BasicNameValuePair("betDate", date));
			}
			String response = hs.getResponseBodyByGet("caipiao.163.com",
					"/order/jczq-hunhe/", nvps);
			List<FootballMatch> matchList = JsoupHtmlParser
					.getFootballMatchList(response);
			FootballMatchDao.batchAddFootballMatch(matchList, existIdSet);

			ExcelUtil.generateDailyFootballMatch(matchList, dir, date);
		}
		
		List<FootballMatch> matchResultList = new ArrayList<FootballMatch>();

		List<String> dateList = FootballMatchDao.getExistMatchDateList(null, null);
		for(String date : dateList){
			List<FootballMatch> matchHistory = FootballMatchDao.getFootballMatchListByDate(date);

			nvps.clear();
			nvps.add(new BasicNameValuePair("startTime", date));
			nvps.add(new BasicNameValuePair("endTime", date));

			String response = hs.getResponseBodyByGet("cp.caipiao365.com",
					"/dc/getKaijiangFootBall.action", nvps);
			matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(response, matchHistory));

			int pages = JsoupHtmlParser.getTotalPagesByCaipiao365(response);
			for (int i = 1; i < pages; i++) {
				nvps.clear();
				nvps.add(new BasicNameValuePair("startTime", date));
				nvps.add(new BasicNameValuePair("endTime", date));
				nvps.add(new BasicNameValuePair("jumpPage", String.valueOf(i + 1)));
				response = hs.getResponseBodyByGet("cp.caipiao365.com", "/dc/getKaijiangFootBall.action", nvps);
				matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(response, matchHistory));
			}
		}
		FootballMatchDao.batchUpdateFootballMatchResult(matchResultList);
	}
	
	/**
	 * 更新某日开始的赛事结果
	 * @param beginDate
	 */
	public static void updateMatchResult(String beginDate) {
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		List<FootballMatch> matchResultList = new ArrayList<FootballMatch>();
		
		List<String> dateList = FootballMatchDao.getExistMatchDateList(beginDate, null);
		for(String date : dateList){
			List<FootballMatch> matchHistory = FootballMatchDao.getFootballMatchListByDate(date);
			
			nvps.clear();
			nvps.add(new BasicNameValuePair("startTime", date));
			nvps.add(new BasicNameValuePair("endTime", date));
			
			String response = hs.getResponseBodyByGet("cp.caipiao365.com",
					"/dc/getKaijiangFootBall.action", nvps);
			matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(response, matchHistory));
			
			int pages = JsoupHtmlParser.getTotalPagesByCaipiao365(response);
			for (int i = 1; i < pages; i++) {
				nvps.clear();
				nvps.add(new BasicNameValuePair("startTime", date));
				nvps.add(new BasicNameValuePair("endTime", date));
				nvps.add(new BasicNameValuePair("jumpPage", String.valueOf(i + 1)));
				response = hs.getResponseBodyByGet("cp.caipiao365.com", "/dc/getKaijiangFootBall.action", nvps);
				matchResultList.addAll(JsoupHtmlParser.getFootballMatchResult(response, matchHistory));
			}
		}
		FootballMatchDao.batchUpdateFootballMatchResult(matchResultList);
	}

}