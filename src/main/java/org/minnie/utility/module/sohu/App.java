package org.minnie.utility.module.sohu;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-11 类说明
 */
public class App {

	private static Logger logger = Logger.getLogger(App.class.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	private static Map<Integer, Integer> phaseMap = new HashMap<Integer, Integer>();

	static {
		phaseMap.put(2014, 26);
		phaseMap.put(2013, 154);
		phaseMap.put(2012, 154);
		phaseMap.put(2011, 153);
		phaseMap.put(2010, 153);
		phaseMap.put(2009, 154);
		phaseMap.put(2008, 154);
		phaseMap.put(2007, 153);
		phaseMap.put(2006, 154);
		phaseMap.put(2005, 153);
		phaseMap.put(2004, 122);
		phaseMap.put(2003, 89);
	};

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
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

		 // 批量添加双色球sql
		 String sqlBatchAddLotteryDoubleColor = null;

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

			sqlBatchAddLotteryDoubleColor = rb.getString("sql.batchAddLottery.doubleColor");
			 logger.info("\t sql.batchAddLottery.doubleColor = " + sqlBatchAddLotteryDoubleColor);

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

		List<DoubleColor> list = new ArrayList<DoubleColor>();

		for (Iterator<Integer> it = phaseMap.keySet().iterator(); it.hasNext();) {
			Integer key = it.next();
			
			Integer startPhase = key * 1000 + 1;
			Integer endPhase = key * 1000 + phaseMap.get(key);
			//方式1
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("startPhase", String.valueOf(startPhase));// 开始期号
//			params.put("endPhase", String.valueOf(endPhase));// 结束期号
//			params.put("phaseOrder", "up");// 期号排序
//			params.put("coldHotOrder", "number");// 冷热号顺序？
//			params.put("onlyBody", "true");// 只显示表格主体？
//			
//			MysqlDatabseHelper.batchAddLotteryDoubleColor(JsoupHtmlParser.getSohuSSQ(Constant.URL_SOHU_LOTTERY_DOUBLE_COLOR,params,key), null);

			//方式2
//			MysqlDatabseHelper.batchAddLotteryDoubleColor(JsoupHtmlParser.getSohuSSQ(startPhase, endPhase, key));
			list.addAll(JsoupHtmlParser.getSohuSSQ(startPhase, endPhase, key));
		}
		
		Collections.sort(list);
		MysqlDatabseHelper.batchAddLotteryDoubleColor(list);
		
	}

}
