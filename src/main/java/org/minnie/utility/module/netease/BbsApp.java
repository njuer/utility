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
	
	private static final Map<Integer,String> authorMap = new HashMap<Integer,String>();
	
	static {
		authorMap.put(667895, "巴乔巴蒂");
		authorMap.put(747026, "彩票活动");
		authorMap.put(732519, "彩票活动发布");
	}

	private static HttpSimulation hs = new HttpSimulation();

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
		init();
//		updateArticles();
	}
	
	public static void init(){
		Set<Integer> keySet = authorMap.keySet();
		Integer[] authorIdArray = keySet.toArray(new Integer[keySet.size()]);
		Map<Integer,String> existThreadMap = MysqlDatabseHelper.getNeteaseThreadTimeMap(authorIdArray);
		
		List<Article> list = initArticleList(authorIdArray);
		List<Article> result = getArticleDetail(list);
		
		MysqlDatabseHelper.batchAddNeteaseArticles(result,existThreadMap);
	}
	
	/**
	 * 初始化文章列表
	 * @param authorIdArray
	 * @return
	 */
	public static List<Article> initArticleList(Integer[] authorIdArray){
		
		long consumerStartTime = System.currentTimeMillis();
		
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
			
//			int i = 0;
			while(true){
//				logger.info("============" + i++);
				String response = hs.getResponseBodyByGet("bbs.caipiao.163.com", "/home.php", nvps);
				list.addAll(JsoupHtmlParser.getArticleList(response, authorId, authorMap));

				/**
				 * 下一页
				 */
				nvps.clear();
				nvps.addAll(JsoupHtmlParser.getParams(response));
				if(nvps.isEmpty()){
					break;
				}
			}
		}
		
		long consumerEndTime = System.currentTimeMillis();
		logger.info("initArticleList运行耗时 : "
				+ (consumerEndTime - consumerStartTime) + "ms");
		return list;
	}
	

	public static void updateArticles(){
		Set<Integer> keySet = authorMap.keySet();
		Integer[] authorIdArray = keySet.toArray(new Integer[keySet.size()]);
		Map<Integer,String> existThreadMap = MysqlDatabseHelper.getNeteaseThreadTimeMap(authorIdArray);
		
		List<Article> list = getLastArticleList(authorIdArray);
		List<Article> result = getArticleDetail(list);
		
		MysqlDatabseHelper.batchAddNeteaseArticles(result,existThreadMap);
	}
	
	public static List<Article> getLastArticleList(Integer[] authorIdArray){
		
		long consumerStartTime = System.currentTimeMillis();
		
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
			
			String response = hs.getResponseBodyByGet("bbs.caipiao.163.com", "/home.php", nvps);
			list.addAll(JsoupHtmlParser.getArticleList(response, authorId, authorMap));
		}
		
		long consumerEndTime = System.currentTimeMillis();
		logger.info("getLastArticleList运行耗时 : "
				+ (consumerEndTime - consumerStartTime) + "ms");
		return list;
	}
	
	
	public static List<Article> getArticleDetail(List<Article> list){

		long consumerStartTime = System.currentTimeMillis();
		
//		HttpSimulation hs = new HttpSimulation();
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
		logger.info("getArticleDetail运行耗时 : "
				+ (consumerEndTime - consumerStartTime) + "ms");
		return result;
	}

}
