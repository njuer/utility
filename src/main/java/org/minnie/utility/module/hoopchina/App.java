package org.minnie.utility.module.hoopchina;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.HtmlUtil;

/**
 * @author 作者名 E-mail:neiplzer@gmail.com
 * @version 创建时间：2014-3-5 下午11:03:18 类说明
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
		
		//HoopChina根目录
		String hoopChinaDirectory = null;

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

			hoopChinaDirectory = rb.getString("hoopchina.directory");
			 logger.info("\t hoopchina.directory = " + hoopChinaDirectory);
				if(null == hoopChinaDirectory || StringUtils.isBlank(hoopChinaDirectory)){
					hoopChinaDirectory = "C:/Entertainment/HoopChina";
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

		getSingleAlbum("http://photo.hupu.com/ent/p11634.html", hoopChinaDirectory);

	}
	
	public static void getSingleAlbum(String urlAddress, String hoopChinaDirectory){
//		String urlAddress = "http://photo.hupu.com/ent/p11634.html";
		HoopChina hoopChina = HtmlUtil.getHoopChina(urlAddress);
		AtomicInteger atomic = new AtomicInteger(0);
		int size = hoopChina.getTotal();
//		logger.info("size = " + size);
		
		// 队列
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(size);
		
		
		long produceStartTime = System.currentTimeMillis();

        ExecutorService produceExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE); 
		// 生产者
		Producer producer = new Producer(queue, produceExecutor, urlAddress, atomic, size);
		
		for(int i = 0; i < THREAD_POOL_SIZE; i++){
			produceExecutor.execute(producer);
		}
		
		produceExecutor.shutdown(); 
        while (!produceExecutor.isTerminated()) {
        	// do nothing
        } 
		long produceEndTime = System.currentTimeMillis();
		logger.info("Finished all producer threads : " + (produceEndTime - produceStartTime) + "ms");
        
		long consumerStartTime = System.currentTimeMillis();
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE); 
		// 消费者
		Consumer consumer = new Consumer(queue, consumerExecutor, hoopChinaDirectory, hoopChina.getTitle());
        
		for(int j = 0; j < THREAD_POOL_SIZE; j++){
			consumerExecutor.execute(consumer);
		}
		consumerExecutor.shutdown(); 
        while (!consumerExecutor.isTerminated()) {
        	// do nothing
        } 
		long consumerEndTime = System.currentTimeMillis();
		logger.info("Finished all consumer threads : " + (consumerEndTime - consumerStartTime) + "ms");
	}
		


}
