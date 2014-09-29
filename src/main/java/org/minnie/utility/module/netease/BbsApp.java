package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;

public class BbsApp {

	private static Logger logger = Logger.getLogger(BbsApp.class.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	private static final int THREAD_POOL_SIZE = 20;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 获取加载系统配置时间
		long configurationStartTime = System.currentTimeMillis();

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);


		
		// 主程序执行
//		int [] authorIdArray = {747026,667895,732519};
		int [] authorIdArray = {747026};
		Set<Integer> existThreadSet = MysqlDatabseHelper.getNeteaseArticleIdSet(authorIdArray);
		
		List<Article> list = getArticleList();
		List<Article> result = getArticleDetail(list);
		
		MysqlDatabseHelper.batchAddNeteaseArticles(result,existThreadSet);
		
//		HttpSimulation hs = new HttpSimulation();
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		String response = hs.getResponseBodyByGet("bbs.caipiao.163.com", "/thread-28561-1-1.html", nvps);
//		JsoupHtmlParser.getArticleDetail(response,null);
	}
	
	public static List<Article> getArticleDetail(List<Article> list){

		long consumerStartTime = System.currentTimeMillis();

		
		HttpSimulation hs = new HttpSimulation();
		List<Article> result = new ArrayList<Article>();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		for(Article article : list){
			Integer threadId = article.getThreadId();
			if(null != threadId){
				nvps.clear();
				String response = hs.getResponseBodyByGet("bbs.caipiao.163.com", "/thread-"+threadId+"-1-1.html", nvps);
				
				article = JsoupHtmlParser.getArticleDetail(response, article);
				
				if(null != article){
					result.add(article);
				}
			}
		}
		
		long consumerEndTime = System.currentTimeMillis();
		logger.info("程序运行耗时 : "
				+ (consumerEndTime - consumerStartTime) + "ms");
		return result;
	}

	public static List<Article> getArticleList(){
		
		long consumerStartTime = System.currentTimeMillis();
		
		List<Article> list = new ArrayList<Article>();
		Map<Integer,String> authorMap = new HashMap<Integer,String>();
//		authorMap.put(667895, "巴乔巴蒂");
		authorMap.put(747026, "彩票活动");
//		authorMap.put(732519, "彩票活动发布");
		
		HttpSimulation hs = new HttpSimulation();
		
		int [] authorArray = {747026, 667895}; 		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		for(int authorId : authorArray){
			nvps.clear();
			
			nvps.add(new BasicNameValuePair("mod", "space"));
			nvps.add(new BasicNameValuePair("uid", String.valueOf(authorId)));
			nvps.add(new BasicNameValuePair("do", "thread"));
			nvps.add(new BasicNameValuePair("thread", "me"));
			nvps.add(new BasicNameValuePair("type", "thread"));
			nvps.add(new BasicNameValuePair("from", "space"));
			nvps.add(new BasicNameValuePair("order", "dateline"));
			nvps.add(new BasicNameValuePair("page", "1"));
			
			String response = hs.getResponseBodyByGet("bbs.caipiao.163.com", "/home.php", nvps);
			
			list.addAll(JsoupHtmlParser.getArticleList(response, authorId, authorMap));
			
		}
		
//		for(Article article : list){
//			logger.info(article);
//		}
		
		long consumerEndTime = System.currentTimeMillis();
		logger.info("程序运行耗时 : "
				+ (consumerEndTime - consumerStartTime) + "ms");
		return list;
	}

}
