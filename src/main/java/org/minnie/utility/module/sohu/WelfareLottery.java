package org.minnie.utility.module.sohu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-13 类说明
 */
public class WelfareLottery {

	private static Logger logger = Logger.getLogger(WelfareLottery.class
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

		List<DoubleColor> list = MysqlDatabseHelper
				.getDoubleColorLotteryList("SELECT * FROM lottery_double_color ORDER BY phase DESC");

		// redAnalyseByLastFive(list);
		// redAnalyseByAllOddAllEven(list);
		// redAnalyseByCutRegion(list);
		// redAnalyseByBlueKillRed(list);
		// redAnalyseByKillMaxMinusMin(list);
		// blueAnalyseByLastRedKillBlue(list);
		// blueAnalyseByAddSubtractionKillBlue(list);
		// blueAnalyseByPhaseillBlue(list);
//		redAnalyseBySameTail(list);
		List<Integer> cutRegion = new ArrayList<Integer>();
		cutRegion.add(3);
		getCandidate(2014027, cutRegion);
	}

	/**
	 * 五期内选红
	 * 
	 * @param list
	 */
	public static void redAnalyseByLastFive(List<DoubleColor> list) {
		int correct = 0;
		int size = list.size();
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			set.clear();
			sb.append(current.getPhase());
			sb.append("\t");

			for (int j = 1; (i + j) < size && j <= 5; j++) {
				DoubleColor ssq = list.get(i + j);
				List<String> ssqList = ssq.getRed();
				for (String red : ssqList) {
					set.add(red);
				}
			}
			List<String> currentRed = current.getRed();
			int hit = 0;
			for (String cred : currentRed) {
				if (set.contains(cred)) {
					sb.append(" ").append(cred);
					hit++;
				} else {
					sb.append(" (").append(cred).append(")");
				}
			}
			sb.append(" ").append(":").append(" ").append(current.getBlue());
			sb.append("\t");
			sb.append(hit);

			if (hit >= 4) {
				correct++;
			}

			logger.info(sb.toString());
		}
		logger.info("正确率：" + StringUtil.correctRate(correct, size));

	}

	/**
	 * 全奇、全偶选红（通常2个月左右开一次）
	 * 
	 * @param list
	 */
	public static void redAnalyseByAllOddAllEven(List<DoubleColor> list) {
		int correct = 0;
		for (DoubleColor ssq : list) {
			StringBuilder sb = new StringBuilder();
			List<String> red = ssq.getRed();
			int odd = 0;
			int even = 0;
			sb.append(ssq.getPhase());
			sb.append("\t");
			for (String cred : red) {
				if (Integer.valueOf(cred) % 2 == 0) {
					even++;
				} else {
					odd++;
				}
				sb.append(cred).append(" ");
			}
			sb.append(": ").append(ssq.getBlue());

			boolean flag = false;

			if (even == 6) {
				sb.append("\t");
				sb.append("EVEN");
				flag = true;
			}

			if (odd == 6) {
				sb.append("\t");
				sb.append("ODD");
				flag = true;
			}

			if (flag) {
				correct++;
			}
			logger.info(sb.toString());
		}
		logger.info("正确率：" + StringUtil.correctRate(correct * 12, list.size()));

	}

	/**
	 * 红球断区
	 * 
	 * @param list
	 */
	public static void redAnalyseByCutRegion(List<DoubleColor> list) {
		int correct = 0;
		for (DoubleColor ssq : list) {
			StringBuilder sb = new StringBuilder();
			List<String> red = ssq.getRed();
			sb.append(ssq.getPhase());
			sb.append("\t");
			int area1 = 0;
			int area2 = 0;
			int area3 = 0;
			int area4 = 0;
			for (String cred : red) {
				switch (Integer.valueOf(cred)) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
					area1++;
					break;
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
					area2++;
					break;
				case 17:
					break;
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
					area3++;
					break;
				case 26:
				case 27:
				case 28:
				case 29:
				case 30:
				case 31:
				case 32:
				case 33:
					area4++;
					break;

				}
				sb.append(cred).append(" ");
			}
			sb.append(": ").append(ssq.getBlue());
			// sb.append("\t");
			boolean flag = false;
			if (area1 == 0) {
				sb.append("\tI");
				flag = true;
			}
			if (area2 == 0) {
				sb.append("\tII");
				flag = true;
			}
			if (area3 == 0) {
				sb.append("\tIII");
				flag = true;
			}
			if (area4 == 0) {
				sb.append("\tIV");
				flag = true;
			}

			if (flag) {
				correct++;
			}

			logger.info(sb.toString());
		}
		logger.info("正确率：" + StringUtil.correctRate(correct, list.size()));
	}

	/**
	 * 篮球杀红
	 * 
	 * @param list
	 */
	public static void redAnalyseByBlueKillRed(List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			List<String> red = current.getRed();
			sb.append(current.getPhase());
			sb.append("\t");
			Set<String> set = new HashSet<String>();
			for (String cred : red) {
				set.add(cred);
				sb.append(cred).append(" ");
			}
			sb.append(": ").append(current.getBlue());

			if ((i + 1) < size) {
				DoubleColor last = list.get(i + 1);
				if (!set.contains(last.getBlue())) {
					sb.append("\tOK");
					correct++;
				}
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 上一期开奖号码的最大号码减去最小号码作为下一期选红的杀码
	 * 
	 * @param list
	 */
	public static void redAnalyseByKillMaxMinusMin(List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			List<String> red = current.getRed();
			sb.append(current.getPhase());
			sb.append("\t");
			Set<String> set = new HashSet<String>();
			for (String cred : red) {
				set.add(cred);
				sb.append(cred).append(" ");
				// int credv = Integer.valueOf(cred);
			}
			// int result = Integer.valueOf(red.get(5)) -
			// Integer.valueOf(red.get(0));

			sb.append(": ").append(current.getBlue());

			if ((i + 1) < size) {
				DoubleColor last = list.get(i + 1);
				List<String> lastRed = last.getRed();
				int result = Integer.valueOf(lastRed.get(5))
						- Integer.valueOf(lastRed.get(0));
				if (!set.contains(StringUtil.getBallValue(result))) {
					sb.append("\tOK");
					correct++;
				}
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 同尾号
	 * 
	 * @param list
	 */
	public static void redAnalyseBySameTail(List<DoubleColor> list) {
		int incorrect = 0;
		int size = list.size();
		for (DoubleColor ssq : list) {
			StringBuilder sb = new StringBuilder();
			List<String> red = ssq.getRed();
			sb.append(ssq.getPhase());
			sb.append("\t");
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (String cred : red) {
				String tail = cred.substring(1);
				Integer value = map.get(tail);
				if (null == value) {
					map.put(tail, 1);
				} else {
					map.put(tail, value + 1);
				}
				sb.append(cred).append(" ");
			}

			sb.append(": ").append(ssq.getBlue());

			Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> entry = it.next();
				String key = entry.getKey();
				Integer value = entry.getValue();
				sb.append("\t").append(key).append("(").append(value)
						.append(")");
				if (value >= 3) {
					incorrect++;
				}
			}

			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(size - incorrect, size));
	}

	/**
	 * 上期红球杀蓝
	 * 
	 * @param list
	 */
	public static void blueAnalyseByLastRedKillBlue(List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			List<String> red = current.getRed();
			sb.append(current.getPhase());
			sb.append("\t");
			for (String cred : red) {
				sb.append(cred).append(" ");
			}
			String blue = current.getBlue();

			sb.append(": ").append(current.getBlue());

			if ((i + 1) < size) {
				DoubleColor last = list.get(i + 1);
				Set<String> set = new HashSet<String>();
				List<String> lastRed = last.getRed();
				for (String lred : lastRed) {
					if (Integer.valueOf(lred) <= 16) {
						set.add(lred);
					}
				}

				if (!set.contains(blue)) {
					sb.append("\tOK");
					correct++;
				}
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 加减法杀蓝
	 * 
	 * @param list
	 */
	public static void blueAnalyseByAddSubtractionKillBlue(
			List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			List<String> red = current.getRed();
			sb.append(current.getPhase());
			sb.append("\t");
			for (String cred : red) {
				sb.append(cred).append(" ");
			}
			String blue = current.getBlue();

			sb.append(": ").append(current.getBlue());

			Set<String> set = new HashSet<String>();
			if ((i + 2) < size) {
				DoubleColor lastOne = list.get(i + 1);
				int lastBlueOne = Integer.valueOf(lastOne.getBlue());

				DoubleColor lastTwo = list.get(i + 2);
				int lastBlueTwo = Integer.valueOf(lastTwo.getBlue());

				int sum = (lastBlueOne + lastBlueTwo) % 10;
				if (sum <= 6) {
					set.add("1" + sum);
				}
				set.add("0" + sum);

				int diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
				if (diff <= 6) {
					set.add("1" + diff);
				}
				set.add("0" + diff);

				lastBlueOne %= 10;
				lastBlueTwo %= 10;

				sum = (lastBlueOne + lastBlueTwo) % 10;
				if (sum <= 6) {
					set.add("1" + sum);
				}
				set.add("0" + sum);

				diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
				if (diff <= 6) {
					set.add("1" + diff);
				}
				set.add("0" + diff);

				if (!set.contains(blue)) {
					sb.append("\tOK");
					correct++;
				}
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 加减法杀蓝
	 * 
	 * @param list
	 */
	public static void blueAnalyseByPhaseKillBlue(List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			DoubleColor current = list.get(i);
			StringBuilder sb = new StringBuilder();
			List<String> red = current.getRed();
			sb.append(current.getPhase());
			sb.append("\t");
			for (String cred : red) {
				sb.append(cred).append(" ");
			}
			String blue = current.getBlue();

			sb.append(": ").append(current.getBlue());

			Set<String> set = new HashSet<String>();

			String phase = String.valueOf(current.getPhase());
			int pNum = Integer.valueOf(phase.substring(6));
			if (pNum <= 6) {
				set.add("1" + pNum);
			}
			set.add("0" + pNum);

			if (!set.contains(blue)) {
				sb.append("\tOK");
				correct++;
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	public static void getCandidate(Integer phase, List<Integer> cutRegion) {

//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT * FROM lottery_double_color ");
//		if (null != phase) {
//			sb.append(" WHERE phase < ").append(phase);
//		}
//		sb.append("ORDER BY phase DESC");

		List<DoubleColor> list = MysqlDatabseHelper.getLastPhase(phase, 5);
		Set<String> redCandidate = new HashSet<String>();
		Set<String> blueCandidate = new HashSet<String>();
		for (DoubleColor ssq : list) {
			List<String> red = ssq.getRed();
			for (String cred : red) {
				// 五期内选红
				redCandidate.add(cred);
			}
		}

		// 初始化蓝球：01~16
		for (int i = 1; i <= 16; i++) {
			blueCandidate.add(StringUtil.getBallValue(i));
		}

		DoubleColor lastPhase = list.get(0);
		// 篮球杀红
		redCandidate.remove(lastPhase.getBlue());

		List<String> lastRed = lastPhase.getRed();
		int result = Integer.valueOf(lastRed.get(5))
				- Integer.valueOf(lastRed.get(0));

		// 最大号码减去最小号码
		redCandidate.remove(StringUtil.getBallValue(result));

		// 红球断区
		int init = 0;
		int boundary = 0;
		for (Integer region : cutRegion) {
			switch (region) {
			case 1:
				init = 1;
				boundary = 8;
				break;
			case 2:
				init = 9;
				boundary = 16;
				break;
			case 3:
				init = 18;
				boundary = 25;
				break;
			case 4:
				init = 26;
				boundary = 33;
				break;
			}
			for (int i = init; i <= boundary; i++) {
				redCandidate.remove(StringUtil.getBallValue(i));
			}
		}

		// 上期红球杀蓝
		for (String lr : lastRed) {
			blueCandidate.remove(lr);
		}

		// 期号杀蓝
		String strPhase = String.valueOf(phase);
		int pTail = Integer.valueOf(strPhase.substring(6));
		blueCandidate.remove("1" + pTail);
		blueCandidate.remove("0" + pTail);

		// 加减法杀蓝
		int lastBlueOne = Integer.valueOf(lastPhase.getBlue());

		DoubleColor lastPhaseTwo = list.get(1);
		int lastBlueTwo = Integer.valueOf(lastPhaseTwo.getBlue());

		int sum = (lastBlueOne + lastBlueTwo) % 10;
		blueCandidate.remove("1" + sum);
		blueCandidate.remove("0" + sum);

		int diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
		blueCandidate.remove("1" + diff);
		blueCandidate.remove("0" + diff);

		lastBlueOne %= 10;
		lastBlueTwo %= 10;

		sum = (lastBlueOne + lastBlueTwo) % 10;
		blueCandidate.remove("1" + sum);
		blueCandidate.remove("0" + sum);

		diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
		blueCandidate.remove("1" + diff);
		blueCandidate.remove("0" + diff);
		
		StringBuilder sb = new StringBuilder();
		sb.append(phase);
		sb.append("\t");
		for(String red:redCandidate){
			sb.append(red);
			sb.append(" ");
		}
		sb.append(":");
		sb.append("\t");
		for(String blue:blueCandidate){
			sb.append(blue);
			sb.append(" ");
		}
		
		logger.info(sb.toString());
	}
	
	public static void doubleColorAnalyse(List<DoubleColor> list){
		
	}

}
