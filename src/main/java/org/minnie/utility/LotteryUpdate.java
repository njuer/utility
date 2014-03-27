package org.minnie.utility;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.module.lottery.FiveInElevenApp;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-27 类说明
 */
public class LotteryUpdate {

	private static Logger logger = Logger.getLogger(LotteryUpdate.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);

		// MysqlDatabseHelper.batchAddLotteryFiveInEleven(ExcelUtil.readDataFrom("D:/lottery/gdd11/130301.xls",
		// 0, "gdd11"));

		/**
		 * 更新11选5
		 */
		HttpSimulation hs = new HttpSimulation();
		Map<String, Integer> map = MysqlDatabseHelper.statsMaxPeriod(null);
		FiveInElevenApp.upgradeAllFiveInEleven(hs, map, "D:/lottery");
	}

}
