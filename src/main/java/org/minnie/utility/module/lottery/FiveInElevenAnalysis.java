package org.minnie.utility.module.lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.entity.lottery.FiveInEleven;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.StringUtil;

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
		
		List<FiveInEleven> list = MysqlDatabseHelper.getFiveInElevenList(Constant.LOTTERY_FIVE_IN_ELEVEN_GUANGDONG, DateUtil.getDate());
		Set<String> exclude = MysqlDatabseHelper.getFiveInElevenAnalysisBySame(null);

		Set<String> set = new HashSet<String>();
		Map<String, Integer> maps = new HashMap<String,Integer>();
		FiveInEleven last = null;
		int size = list.size();
		for(int i = size - 1; i >=0 ; i--){
			FiveInEleven fie = list.get(i);
			if(i == size -1){
				last = fie;
			}
			String lotteryNumber = fie.getLotteryNumber();
			if(exclude.contains(lotteryNumber)){
				continue;
			}
			int hits = FiveInElevenApp.analyseCandidateHit(last, fie,1,3);
			
			if(hits > 0){
				if(Math.abs(last.getPeriod() - fie.getPeriod()) > 30){
					continue;
				}
				
				logger.info(fie.getPeriod() + " : " +lotteryNumber + "\t(" + hits + ")");
					Matcher matcher = StringUtil.ballPattern.matcher(lotteryNumber);
					while (matcher.find()) {
						String hit = matcher.group();
						Integer count = maps.get(hit);
						if(null != count){
							maps.put(hit, Integer.valueOf(count.intValue()+1));
						} else {
							maps.put(hit, 1);
						}
					}
			}
//			boolean candidate = FiveInElevenApp.analyseCandidate(last, fie,2,3);
//			
//			if(candidate){
//				if(Math.abs(last.getPeriod() - fie.getPeriod()) > 30){
//					continue;
//				}
//				
//				logger.info(fie.getPeriod() + " : " +lotteryNumber);
//				Matcher matcher = StringUtil.ballPattern.matcher(lotteryNumber);
//				while (matcher.find()) {
//					String hit = matcher.group();
//					Integer count = maps.get(hit);
//					if(null != count){
//						maps.put(hit, Integer.valueOf(count.intValue()+1));
//					} else {
//						maps.put(hit, 1);
//					}
//				}
//			}
		}
		
		Iterator<Entry<String, Integer>> iter = maps.entrySet().iterator(); 
		String keys = "";
		String vals = "";
		List<String> result = new ArrayList<String>();
		while (iter.hasNext()) { 
		    Entry<String, Integer> entry = iter.next(); 
		    String key = entry.getKey(); 
		    Integer val = entry.getValue(); 
		    result.add(key);
//		    keys += key + "\t";
//		    vals += val + "\t";
//		    logger.info(key + "\t" + val);
		}  
		Collections.sort(result);
		for(String key : result){
		    keys += key + "\t";
		    vals += maps.get(key) + "\t";
		}
		
		logger.info(keys);
		logger.info(vals);
//		for(FiveInEleven fie : list){
//			set.add(fie.getLotteryNumber());
//		}
		
//		Set<String> exclude = MysqlDatabseHelper.getFiveInElevenAnalysisBySame(null);
//		set.removeAll(exclude);
//		
//		for(String number : set){
//			logger.info(number);
//		}
		
	}

}
