package org.minnie.utility;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.FileUtil;

/**
 * 照片下载
 * 
 */
public class App {
	
	private static Logger logger = Logger.getLogger(App.class.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	private static final int DOMAIN_THREAD_POOL_SIZE = 3;
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

		logger.info("程序开始执行时间：" + DateUtil.getTime(configurationStartTime));

		// 网易目录
		String neteaseDirectory = null;

		// 网易图集 URL文件目录
		String neteaseUrlPath = null;

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

			neteaseDirectory = rb.getString("netease.directory");
			logger.info("\t netease.directory = " + neteaseDirectory);
			if (null == neteaseDirectory
					|| StringUtils.isBlank(neteaseDirectory)) {
				neteaseDirectory = "C:/Entertainment/Netease";
			}

			neteaseUrlPath = rb.getString("netease.url.path");
			logger.info("\t netease.url.path = " + neteaseUrlPath);
			if (null == neteaseUrlPath || StringUtils.isBlank(neteaseUrlPath)) {
				neteaseUrlPath = "C:/netease.txt";
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

		Set<String> set = FileUtil.getNeteaseUrlSet(neteaseUrlPath);
		
		Set<String> neteaseSet = new HashSet<String>();
		Set<String> hoopChinaSet = new HashSet<String>();
		Set<String> yangShengSuoSet = new HashSet<String>();
		
		for(String galleryUrl : set){
			if(Constant.DOMAIN_163.equals(galleryUrl)){
				neteaseSet.add(galleryUrl);
			} else if(Constant.DOMAIN_HUPU.equals(galleryUrl) || Constant.DOMAIN_HOOPCHINA.equals(galleryUrl)){
				hoopChinaSet.add(galleryUrl);
			} else if(Constant.DOMAIN_39YANGSHENGSUO.equals(galleryUrl) || Constant.DOMAIN_TIANTIANYULE.equals(galleryUrl)){
				yangShengSuoSet.add(galleryUrl);
			}
		}
		
		
		
	}
}
