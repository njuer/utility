package org.minnie.utility.module.lottery;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.LotteryUpdate;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-29
 * 11选5分析
 */
public class FiveInElevenAnalysis {
	
	private static Logger logger = Logger.getLogger(FiveInElevenAnalysis.class.getName());
	
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
		
		/**
		 * 更新11选5
		 */
		HttpSimulation hs = new HttpSimulation();
		Map<String, Integer> map = MysqlDatabseHelper.statsMaxPeriod(null);
		FiveInElevenApp.upgradeAllFiveInEleven(hs, map, "D:/lottery");
		
		FiveInElevenApp.fiveInElevenAnaylse(Constant.LOTTERY_FIVE_IN_ELEVEN_GUANGDONG);
		
	}

}
