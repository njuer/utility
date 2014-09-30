package org.minnie.utility.scheduler.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.minnie.utility.entity.lottery.ShiShiCaiPredict;
import org.minnie.utility.module.netease.Article;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NeteaseBbsJob implements Job {

	
	private static Logger logger = Logger.getLogger(NeteaseBbsJob.class
			.getName());

	public static final String PARAM_HTTPSIMULATION = "hs";
	public static final String PARAM_NVPS = "nvps";
	public static final String PARAM_MAIL_SESSION = "session";
	public static final String PARAM_MAIL_HOST = "host";
	public static final String PARAM_MAIL_USER = "user";
	public static final String PARAM_MAIL_PWD = "pwd";
	public static final String PARAM_MAIL_TO = "to";

	public static final String HOST_NETEASE_BBS = "bbs.caipiao.163.com";
	
	public static final Map<Integer,String> authorMap = new HashMap<Integer,String>();
	
	static {
		authorMap.put(667895, "巴乔巴蒂");
		authorMap.put(747026, "彩票活动");
		authorMap.put(732519, "彩票活动发布");
	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("定时任务运行时间："+ new Date(System.currentTimeMillis()));
		
		JobDataMap data = context.getMergedJobDataMap();
		HttpSimulation hs = (HttpSimulation)data.get(PARAM_HTTPSIMULATION);
		Session session = (Session)data.get(PARAM_MAIL_SESSION);
		String host = data.getString(PARAM_MAIL_HOST);
		String user = data.getString(PARAM_MAIL_USER);
		String pwd = data.getString(PARAM_MAIL_PWD);
		String [] to = (String [])data.get(PARAM_MAIL_TO);

		/**
		 * 业务逻辑开始
		 */
		Set<Integer> keySet = authorMap.keySet();
		Integer[] authorIdArray = keySet.toArray(new Integer[keySet.size()]);
		Map<Integer,String> existThreadMap = MysqlDatabseHelper.getNeteaseThreadTimeMap(authorIdArray);
		
		/**
		 * 获取文章列表
		 */
		List<Article> list = new ArrayList<Article>();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		for(Integer authorId : authorIdArray){
			
			nvps.clear();
			/**
			 * 第一页
			 */
			nvps.add(new BasicNameValuePair("mod", "space"));
			nvps.add(new BasicNameValuePair("uid", String.valueOf(authorId)));
			nvps.add(new BasicNameValuePair("do", "thread"));
			nvps.add(new BasicNameValuePair("thread", "me"));
			nvps.add(new BasicNameValuePair("type", "thread"));
			nvps.add(new BasicNameValuePair("from", "space"));
			nvps.add(new BasicNameValuePair("order", "dateline"));
			nvps.add(new BasicNameValuePair("page", "1"));
			
			String response = hs.getResponseBodyByGet(HOST_NETEASE_BBS, "/home.php", nvps);
			list.addAll(JsoupHtmlParser.getArticleList(response, authorId, authorMap));
		}
		
		/**
		 * 获取文章详细信息
		 */
		List<Article> detailList = new ArrayList<Article>();
		for(Article article : list){
			Integer threadId = article.getThreadId();
			if(null != threadId){
				nvps.clear();
				String response = hs.getResponseBodyByGet(HOST_NETEASE_BBS, "/thread-"+threadId+"-1-1.html", nvps);
				
				article = JsoupHtmlParser.getArticleDetail(response, article);
				
				if(null != article){
					detailList.add(article);
				}
			}
		}
		
		/**
		 * 发送邮件
		 */
		List<Article> mailList = MysqlDatabseHelper.persistAndToMail(detailList,existThreadMap);
		for(Article atc : mailList){
			MailUtil.sendMailByHtml(session, host, user, pwd, to, atc.getSubject(), atc.getContent());
			logger.info("mail sent, subject [" + atc.getSubject() + "]");
		}
	}

}