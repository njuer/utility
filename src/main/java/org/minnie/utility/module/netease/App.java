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

		Set<String> set = FileUtil.getNeteaseUrlList(neteaseUrlPath);
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
				neteaseDirectory);

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

		// // String json = "\"info\": {";
		// String json = "{";
		// json += "\"setname\": \"NBA常规赛：湖人114-110雷霆\",";
		// json += "\"imgsum\": 20,";
		// json += "\"lmodify\": \"2014-03-10 07:46:31\",";
		// json +=
		// "\"prevue\": \"洛杉矶湖人队（22胜42负）成功在主场止血。尽管杜兰特得到27分、12次助攻和10个篮板的三双表现，维斯布鲁克也有20分、8次助攻和7个篮板，但米克斯得到生涯新高42分，加索尔也有20分和11个篮板，他们率队在第三节打出反扑高潮逆转最多18分落后，湖人队第四节成功保住优势，他们在主场以114-110战胜俄克拉荷马城雷霆队（46胜17负）。湖人队结束三连败，雷霆队遭遇两连败。\",";
		// json += "\"channelid\": \"0005\",";
		// json += "\"prev\": {";
		// json += "\"setname\": \"NBA常规赛：公牛95-88热火\",";
		// json +=
		// "\"simg\": \"http://img4.cache.netease.com/photo/0005/2014-03-10/s_9MUSSCFH4TM10005.jpg\",";
		// json +=
		// "\"seturl\": \"http://sports.163.com/photoview/4TM10005/111570.html\"";
		// json += "},";
		// json += "\"next\": {";
		// json += "\"setname\": \"NBA常规赛：快船 - 老鹰\",";
		// json +=
		// "\"simg\": \"http://img2.cache.netease.com/photo/0005/2014-03-09/s_9MT8BI0U4TM10005.jpg\",";
		// json +=
		// "\"seturl\": \"http://sports.163.com/photoview/4TM10005/111543.html\"";
		// json += "}";
		//
		// json += "}";
		//
		// // 接收{}对象，此处接收数组对象会有异常
		// if (json.indexOf("[") != -1) {
		// json = json.replace("[", "");
		// }
		// if (json.indexOf("]") != -1) {
		// json = json.replace("]", "");
		// }
		// JSONObject obj = new JSONObject().fromObject(json);
		// Gallery gallery = (Gallery) JSONObject.toBean(obj, Gallery.class);
		// System.out.println("obj: " + gallery.toString());

		// =====================================================================================

		// String json = "{";
		// json += "\"id\": \"9MV87AUB4TM10005\",";
		// json +=
		// "\"img\": \"http://img3.cache.netease.com/photo/0005/2014-03-10/9MV87AUB4TM10005.jpg\",";
		// json +=
		// "\"timg\": \"http://img3.cache.netease.com/photo/0005/2014-03-10/t_9MV87AUB4TM10005.jpg\",";
		// json +=
		// "\"simg\": \"http://img3.cache.netease.com/photo/0005/2014-03-10/s_9MV87AUB4TM10005.jpg\",";
		// json +=
		// "\"oimg\": \"http://img3.cache.netease.com/photo/0005/2014-03-10/9MV87AUB4TM10005.jpg\",";
		// json += "\"osize\": {\"w\":1280,\"h\":846},";
		// json += "\"title\": \"杜兰特很无奈\",";
		// json += "\"note\": \"\",";
		// json += "\"newsurl\": \"#\"";
		// json += "}";
		//
		// // 接收{}对象，此处接收数组对象会有异常
		// if (json.indexOf("[") != -1) {
		// json = json.replace("[", "");
		// }
		// if (json.indexOf("]") != -1) {
		// json = json.replace("]", "");
		// }
		// JSONObject obj = new JSONObject().fromObject(json);
		// Picture picture = (Picture) JSONObject.toBean(obj,
		// Picture.class);
		// System.out.println("obj: " + picture.toString());

	}

}
