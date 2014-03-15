package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10 基于Jsoup的网易图集下载
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
		
		
		// 主程序执行
		runNeteaseApp(set, neteaseDirectory);
	}
	
	public static void runNeteaseApp(Set<String> set, String directory){
		LinkedBlockingQueue<Netease> pictureUrlQueue = new LinkedBlockingQueue<Netease>();

		for (String galleryUrl : set) {
			NeteasePage np = JsoupHtmlParser.getNeteasePage(galleryUrl);
			if (null == np) {
				continue;
			}

			Gallery info = np.getInfo();
			if (null == info) {
				continue;
			}

			String subject = info.getSetname();
			int sum = info.getImgsum();
			List<Picture> list = np.getList();
			for (Picture pict : list) {
				Netease netease = new Netease();
				netease.setId(pict.getId());
				netease.setImg(pict.getImg());
				netease.setTitle(pict.getTitle());
				netease.setSize(sum);
				netease.setSubject(subject);

				try {
					pictureUrlQueue.put(netease);
				} catch (InterruptedException e) {
					logger.error("InterruptedException[App->main]: "
							+ e.getMessage());
				}
			}

		}

		long consumerStartTime = System.currentTimeMillis();
		ExecutorService pictureURLExecutor = Executors
				.newFixedThreadPool(THREAD_POOL_SIZE);
		// 消费者
		PictureGenerator pg = new PictureGenerator(pictureUrlQueue,
				directory);

		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			pictureURLExecutor.execute(pg);
		}

		pictureURLExecutor.shutdown();
		while (!pictureURLExecutor.isTerminated()) {
			// do nothing
		}
		long consumerEndTime = System.currentTimeMillis();

		logger.info("Finished all consumer threads : "
				+ (consumerEndTime - consumerStartTime) + "ms");
	}

}
