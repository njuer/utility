package org.minnie.utility.module.netease;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.util.Constant;

public class RandomMatch {
	
	private static Logger logger = Logger.getLogger(RandomMatch.class.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	public static void main(String[] args) {
		// 获取加载系统配置时间
		long configurationStartTime = System.currentTimeMillis();

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);
		
		
//		List<Integer> list = new ArrayList<Integer>();
//		list.add(e);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("002", 1);
		
		
		
	}
	
	public static void getRandomMatch(Map<String, Integer> map, int num){
		Iterator<Entry<String, Integer>> set = map.entrySet().iterator();
		
	}

}
