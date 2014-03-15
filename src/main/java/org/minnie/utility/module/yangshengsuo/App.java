package org.minnie.utility.module.yangshengsuo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.FileUtil;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-8
 * 39养生所、天天娱乐网
 */
public class App {

	private static Logger logger = Logger.getLogger(App.class.getName());
	
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

		logger.info("程序开始执行时间：" + DateUtil.getTime(configurationStartTime));

		// 39养身所根目录
		String yssDirectory = null;

		// 39养身所  URL 文件目录
		String yssUrlPath = null;

		/**
		 * 加载系统参数
		 */
		String confFilePath = System.getProperty("user.dir")
				+ Constant.SYS_PARAM_FILE;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					confFilePath));
			rb = new PropertyResourceBundle(inputStream);

			logger.info("加载系统参数......");

			yssDirectory = rb.getString("yangshengsuo.directory");
			logger.info("\t yangshengsuo.directory = " + yssDirectory);
			if (null == yssDirectory
					|| StringUtils.isBlank(yssDirectory)) {
				yssDirectory = "C:/Entertainment/YangShengSuo";
			}

			yssUrlPath = rb.getString("yangshengsuo.url.path");
			logger.info("\t yangshengsuo.url.path = " + yssUrlPath);
			if (null == yssUrlPath
					|| StringUtils.isBlank(yssUrlPath)) {
				yssUrlPath = "C:/yangshengsuo.txt";
			}

			// 关闭inputStream
			if (null != inputStream) {
				inputStream.close();
			}
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage());
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}

		// 获取加载系统配置结束时间
		long configurationEndTime = System.currentTimeMillis();

		logger.info("加载系统配置耗时 "
				+ (configurationEndTime - configurationStartTime) + "ms");
		
		//获取39养生所页面地址
		Set<String> set = FileUtil.getYangShengSuoUrlSet(yssUrlPath);
		
		run39YangShengSuoApp(set, yssDirectory);
		
	}
	
	public static void run39YangShengSuoApp(Set<String> set, String directory){
		//页面队列
		LinkedBlockingQueue<Regimen> pageUrlQueue = new LinkedBlockingQueue<Regimen>();
		
		for (String urlAddress : set) {
			
			urlAddress = StringUtil.getUrl(urlAddress);
			
			if(null != urlAddress){
				JsoupHtmlParser.extractPagesBy39YangShengsuo(urlAddress, pageUrlQueue);
			}
		}
		
		LinkedBlockingQueue<Regimen> pictureUrlQueue = new LinkedBlockingQueue<Regimen>();

		PictureUrlGenerator pug = new PictureUrlGenerator(pageUrlQueue, pictureUrlQueue);
		PictureGenerator pg = new PictureGenerator(pictureUrlQueue, directory);

		ExecutorService pictureExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			pictureExecutor.execute(pug);
			i++;
			pictureExecutor.execute(pg);
			i++;
			pictureExecutor.execute(pg);
		}

		pictureExecutor.shutdown();
		while (!pictureExecutor.isTerminated()) {
			// do nothing
		}
		logger.info("图片下载完成！");
	}

}
