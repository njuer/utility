package org.minnie.utility.module.sohu;

import java.util.ArrayList;
import java.util.Collections;
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
import org.minnie.utility.util.ExcelUtil;

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

		// List<DoubleColor> list = MysqlDatabseHelper
		// .getDoubleColorLotteryList("SELECT * FROM lottery_double_color ORDER BY phase DESC");
//		Map<Integer, DoubleColor> map = MysqlDatabseHelper
//				.getDoubleColorLotteryMap("SELECT * FROM lottery_double_color ORDER BY phase DESC");

		// redAnalyseByLastFive(list);
		// redAnalyseByAllOddAllEven(list);
		// redAnalyseByCutRegion(list);
		// redAnalyseByBlueKillRed(list);
		// redAnalyseByKillMaxMinusMin(list);
		// blueAnalyseByLastRedKillBlue(list);
		// blueAnalyseByAddSubtractionKillBlue(list);
		// blueAnalyseByPhaseillBlue(list);
		// redAnalyseBySameTail(list);
		// List<Integer> cutRegion = new ArrayList<Integer>();
		// cutRegion.add(3);
		// getCandidate(2014029, cutRegion);
		// doubleColorAnalyse(list);
		// doubleColorAnalyse(map);

//		SummaryHSSF.generateAnalysis(getDoubleColorAnalyse(map),
//				"C:/create.xlsx");
		// SummaryHSSF.generateAnalysis(null, "C:/Analyse.xlsx");
//		SummaryHSSF.generateFullAnalysis(null, "C:/temp/Analyse.xlsx");
		ExcelUtil.generateFullAnalysis(doubleColorAnalyse(2003001, null), "C:/temp/Analyse.xlsx");
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
	 * 期号杀蓝
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

		// StringBuilder sb = new StringBuilder();
		// sb.append("SELECT * FROM lottery_double_color ");
		// if (null != phase) {
		// sb.append(" WHERE phase < ").append(phase);
		// }
		// sb.append("ORDER BY phase DESC");

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
		List<String> redCandidateList = new ArrayList<String>();
		for (String red : redCandidate) {
			redCandidateList.add(red);
		}
		Collections.sort(redCandidateList);
		for (String red : redCandidateList) {
			sb.append(red);
			sb.append(" ");
		}
		sb.append("(").append(redCandidateList.size()).append(")");
		sb.append(":");
		sb.append("\t");
		List<String> blueCandidateList = new ArrayList<String>();
		for (String blue : blueCandidate) {
			blueCandidateList.add(blue);
		}
		Collections.sort(blueCandidateList);
		for (String blue : blueCandidateList) {
			sb.append(blue);
			sb.append(" ");
		}
		sb.append("(").append(blueCandidateList.size()).append(")");

		logger.info(sb.toString());
	}

	/**
	 * 双色球分析
	 * 
	 * @param map
	 */
	public static void doubleColorAnalyse(Map<Integer, DoubleColor> map) {
		List<Integer> list = new ArrayList<Integer>();

		Iterator<Entry<Integer, DoubleColor>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, DoubleColor> entry = it.next();
			list.add(entry.getKey());
		}

		// list排序
		Collections.sort(list);

		int size = list.size();
		// int correct = 0;
		for (int i = 0; i < size; i++) {
			// 获取期号
			Integer phase = list.get(i);
			// 获取本期数据
			DoubleColor current = map.get(phase);
			if (null != current) {
				StringBuilder sb = new StringBuilder();
				sb.append(phase);
				sb.append("\t");
				Set<String> redCurrent = new HashSet<String>();
				String blueCurrent = current.getBlue();

				// 获取当前红球列表
				List<String> redList = current.getRed();
				for (String red : redList) {
					redCurrent.add(red);
					sb.append(red);
					sb.append(" ");
				}

				sb.append(": ");
				sb.append(blueCurrent);

				// 五期内选红命中
				int hit = lastFivePhaseForRed(i, current, list, map);
				sb.append("\t五期内选红命中: ").append(hit);

				// 下移号命中
				hit = downForRed(i, current, list, map);
				sb.append("\t下移号命中: ").append(hit);

				// 上期篮球杀红命中
				hit = lastBlueKillRed(i, current, list, map);
				sb.append("\t上期篮球杀红命中: ").append(hit);

				// 上期红球最大号码减去最小号码杀红球命中
				hit = lastMaxMinusMinKillRed(i, current, list, map);
				sb.append("\t上期红球最大号码减去最小号码杀红球命中: ").append(hit);

				// 期号杀蓝 命中
				hit = phaseNumberKillBlue(i, current, list, map);
				sb.append("\t期号杀蓝命中: ").append(hit);

				// 加减法杀蓝 命中
				hit = addSubtractionKillBlue(i, current, list, map);
				sb.append("\t加减法杀蓝命中: ").append(hit);

				logger.info(sb.toString());
			} // end of if(null != current)
		} // end of for (int i = 0; i < size; i++)
			// Correct rate
			// logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 双色球分析
	 * 
	 * @param map
	 */
	public static List<DoubleColorAnalyse> doubleColorAnalyse(Integer startPhase, Integer endPhase) {

		List<DoubleColorAnalyse> analyseList = new ArrayList<DoubleColorAnalyse>();

		if (null == startPhase) {
			logger.info("没有设置startPhase");
			return analyseList;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM lottery_double_color");
		sql.append(" WHERE phase >= ").append(startPhase);
		if (null != endPhase) {
			sql.append(" AND phase <= ").append(endPhase);
		}
		sql.append(" ORDER BY phase DESC");

		Map<Integer, DoubleColor> map = MysqlDatabseHelper
				.getDoubleColorLotteryMap(sql.toString());

		List<Integer> list = new ArrayList<Integer>();
		Iterator<Entry<Integer, DoubleColor>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, DoubleColor> entry = it.next();
			list.add(entry.getKey());
		}

		// list排序
		Collections.sort(list);

		int size = list.size();
		// int correct = 0;
		for (int i = 0; i < size; i++) {
			// 获取期号
			Integer phase = list.get(i);
			// 获取本期数据
			DoubleColor current = map.get(phase);
			if (null != current) {
				DoubleColorAnalyse ssqAnalyse = new DoubleColorAnalyse();
				Set<String> currentRedSet = new HashSet<String>();
				// 获取当前红球列表
				List<String> redList = current.getRed();
				for (String red : redList) {
					currentRedSet.add(red);
				}

				String currentBlue = current.getBlue();

				ssqAnalyse.setDoubleColor(current);

				// 备选红球集合
				Set<String> redCandidate = new HashSet<String>();
				// 备选蓝球集合
				Set<String> blueCandidate = new HashSet<String>();
				// 初始化蓝球：01~16
				for (int k = 1; k <= 16; k++) {
					blueCandidate.add(StringUtil.getBallValue(k));
				}

				if (i < 1) {
					continue;
				}
				// 获取上期数据
				Integer lastPhase = list.get(i - 1);
				DoubleColor last = map.get(lastPhase);
				if (null != last) {
					Set<String> lastRedSet = new HashSet<String>();
					String lastBlue = last.getBlue();
					// 获取上期红球列表
					List<String> lastRedList = last.getRed();
					for (String red : lastRedList) {
						lastRedSet.add(red);
					} // end of for (String red : lastRedList)
					redCandidate.addAll(lastRedSet);

					// 下移号命中
					Set<String> resultSet = new HashSet<String>();
					resultSet.addAll(currentRedSet);
					if (resultSet.retainAll(lastRedSet)) {
						ssqAnalyse.setDownForRed(resultSet.size());
					}

					// 上期篮球杀红
					if (!currentRedSet.contains(lastBlue)) {
						ssqAnalyse.setLastBlueKillRed(1);
					}

					// 上期红球最大号码减去最小号码杀红球
					int result = Integer.valueOf(lastRedList.get(5))
							- Integer.valueOf(lastRedList.get(0));
					String killRed = StringUtil.getBallValue(result);
					if (!currentRedSet.contains(killRed)) {
						ssqAnalyse.setLastMaxMinusMinKillRed(1);
					}

					// 期号杀蓝
					String tail = String.valueOf(phase).substring(6);
					if (!tail.equals(currentBlue.substring(1))) {
						blueCandidate.remove("0" + tail);
						blueCandidate.remove("1" + tail);
						ssqAnalyse.setPhaseNumberKillBlue(1);
					}

					// 加减法杀蓝
					if (i < 2) {
						continue;
					}
					int lastBlueOne = Integer.valueOf(lastBlue);

					Integer lastPhaseTwo = list.get(i - 2);
					DoubleColor lastTwo = map.get(lastPhaseTwo);
					int lastBlueTwo = Integer.valueOf(lastTwo.getBlue());
					List<String> lastTwoRedList = lastTwo.getRed();
					for (String red : lastTwoRedList) {
						redCandidate.add(red);
					}

					Set<String> killBlueSet = new HashSet<String>();

					int sum = (lastBlueOne + lastBlueTwo) % 10;
					killBlueSet.add("0" + sum);
					killBlueSet.add("1" + sum);

					int diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
					killBlueSet.add("0" + diff);
					killBlueSet.add("1" + diff);

					lastBlueOne %= 10;
					lastBlueTwo %= 10;

					sum = (lastBlueOne + lastBlueTwo) % 10;
					killBlueSet.add("0" + sum);
					killBlueSet.add("1" + sum);

					diff = Math.abs(lastBlueOne - lastBlueTwo) % 10;
					killBlueSet.add("0" + diff);
					killBlueSet.add("1" + diff);

					if (!killBlueSet.contains(currentBlue)) {
						ssqAnalyse.setAddSubtractionKillBlue(1);
					}
					blueCandidate.removeAll(killBlueSet);
					
					// 五期内选红
					if (i < 5) {
						continue;
					}
					
					for(int j = 3; j <=5; j++){
						Integer cPhase = list.get(i - j);
						DoubleColor cDoubleColor = map.get(cPhase);
						List<String> cRedList = cDoubleColor.getRed();
						for (String red : cRedList) {
							redCandidate.add(red);
						}
					}
					
					int hit = 0;
					for (String red : currentRedSet) {
						if (redCandidate.contains(red)) {
							hit++;
						}
					}
					
					// 上期篮球杀红
					redCandidate.remove(lastBlue);
					// 上期红球最大号码减去最小号码杀红球
					redCandidate.remove(killRed);
					
					ssqAnalyse.setLastFivePhaseForRed(hit);
				}

				analyseList.add(ssqAnalyse);

			} // end of if(null != current)
		} // end of for (int i = 0; i < size; i++)
		return analyseList;
	}

	/**
	 * 双色球分析结果清单
	 * 
	 * @param map
	 */
	public static List<DoubleColorAnalyse> getDoubleColorAnalyse(
			Map<Integer, DoubleColor> map) {
		List<Integer> list = new ArrayList<Integer>();

		Iterator<Entry<Integer, DoubleColor>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, DoubleColor> entry = it.next();
			list.add(entry.getKey());
		}

		// list排序
		Collections.sort(list);

		List<DoubleColorAnalyse> analyseList = new ArrayList<DoubleColorAnalyse>();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			// 获取期号
			Integer phase = list.get(i);
			// 获取本期数据
			DoubleColor current = map.get(phase);
			if (null != current) {
				// StringBuilder sb = new StringBuilder();
				// sb.append(phase);
				// sb.append("\t");
				// Set<String> redCurrent = new HashSet<String>();
				// String blueCurrent = current.getBlue();
				//
				// // 获取当前红球列表
				// List<String> redList = current.getRed();
				// for (String red : redList) {
				// redCurrent.add(red);
				// sb.append(red);
				// sb.append(" ");
				// }
				//
				// sb.append(": ");
				// sb.append(blueCurrent);

				DoubleColorAnalyse ssqAnalyse = new DoubleColorAnalyse();
				ssqAnalyse.setDoubleColor(current);

				// 五期内选红命中
				// int hit = lastFivePhaseForRed(i, current, list, map);
				// sb.append("\t五期内选红命中: ").append(hit);
				ssqAnalyse.setLastFivePhaseForRed(lastFivePhaseForRed(i,
						current, list, map));

				// 下移号命中
				// hit = downForRed(i, current, list, map);
				// sb.append("\t下移号命中: ").append(hit);
				ssqAnalyse.setDownForRed(downForRed(i, current, list, map));

				// 上期篮球杀红命中
				// hit = lastBlueKillRed(i, current, list, map);
				// sb.append("\t上期篮球杀红命中: ").append(hit);
				ssqAnalyse.setLastBlueKillRed(lastBlueKillRed(i, current, list,
						map));

				// 上期红球最大号码减去最小号码杀红球命中
				// hit = lastMaxMinusMinKillRed(i, current, list, map);
				// sb.append("\t上期红球最大号码减去最小号码杀红球命中: ").append(hit);
				ssqAnalyse.setLastMaxMinusMinKillRed(lastMaxMinusMinKillRed(i,
						current, list, map));

				// 期号杀蓝 命中
				// hit = phaseNumberKillBlue(i, current, list, map);
				// sb.append("\t期号杀蓝命中: ").append(hit);
				ssqAnalyse.setPhaseNumberKillBlue(phaseNumberKillBlue(i,
						current, list, map));

				// 加减法杀蓝 命中
				// hit = addSubtractionKillBlue(i, current, list, map);
				// sb.append("\t加减法杀蓝命中: ").append(hit);
				ssqAnalyse.setAddSubtractionKillBlue(addSubtractionKillBlue(i,
						current, list, map));

				analyseList.add(ssqAnalyse);
				// logger.info(sb.toString());
			} // end of if(null != current)
		} // end of for (int i = 0; i < size; i++)
			// Correct rate
			// logger.info("正确率：" + StringUtil.correctRate(correct, size));
		return analyseList;
	}

	public static void doubleColorAnalyse(List<DoubleColor> list) {
		int size = list.size();
		int correct = 0;
		for (int i = 0; i < size; i++) {
			StringBuilder sb = new StringBuilder();
			DoubleColor current = list.get(i);
			Set<String> redCurrent = new HashSet<String>();
			String blueCurrent = current.getBlue();

			Integer phase = current.getPhase();

			sb.append(phase);
			sb.append("\t");

			List<String> redList = current.getRed();
			for (String red : redList) {
				redCurrent.add(red);
				sb.append(red);
				sb.append(" ");
			}
			sb.append(": ");
			sb.append(blueCurrent);

			Set<String> redCandidate = new HashSet<String>();
			Set<String> blueCandidate = new HashSet<String>();
			// 初始化蓝球：01~16
			for (int k = 1; k <= 16; k++) {
				blueCandidate.add(StringUtil.getBallValue(k));
			}

			// 五期内选红
			for (int j = 1; (i + j) < size && j <= 5; j++) {
				DoubleColor ssq = list.get(i + j);
				List<String> ssqList = ssq.getRed();
				for (String red : ssqList) {
					redCandidate.add(red);
				}
			}

			if (i + 2 > size) {
				continue;
			}

			DoubleColor lastPhase = list.get(i + 1);
			// 篮球杀红
			redCandidate.remove(lastPhase.getBlue());

			// 最大号码减去最小号码
			List<String> lastRed = lastPhase.getRed();
			int result = Integer.valueOf(lastRed.get(5))
					- Integer.valueOf(lastRed.get(0));
			redCandidate.remove(StringUtil.getBallValue(result));

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

			if (i + 3 > size) {
				continue;
			}
			DoubleColor lastPhaseTwo = list.get(i + 2);
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

			int redHit = 0;
			sb.append("\t候选号\t");
			List<String> redCandidateList = new ArrayList<String>();
			for (String red : redCandidate) {
				redCandidateList.add(red);
			}
			Collections.sort(redCandidateList);
			for (String red : redCandidateList) {
				if (redCurrent.contains(red)) {
					sb.append("(").append(red).append(")");
					redHit++;
				} else {
					sb.append(red);
				}
				sb.append(",");
			}
			sb.append("\t红球命中： ").append(redHit);
			sb.append("\t:\t");
			boolean blueHit = false;
			List<String> blueCandidateList = new ArrayList<String>();
			for (String blue : blueCandidate) {
				blueCandidateList.add(blue);
			}
			Collections.sort(blueCandidateList);
			for (String blue : blueCandidateList) {
				if (blueCurrent.equals(blue)) {
					sb.append("(").append(blue).append(")");
					blueHit = true;
				} else {
					sb.append(blue);
				}
				sb.append(",");
			}
			sb.append("\t蓝球球命中： ");
			if (blueHit) {
				sb.append(1);
			} else {
				sb.append(0);
			}

			sb.append("\t结果：\t");
			if (blueHit) {
				switch (redHit) {
				case 0:
				case 1:
				case 2:
					sb.append("六等奖(5元)");
					correct++;
					break;
				case 3:
					sb.append("五等奖(10元)");
					correct++;
					break;
				case 4:
					sb.append("四等奖(200元)");
					correct++;
					break;
				case 5:
					sb.append("三等奖(3000元)");
					correct++;
					break;
				case 6:
					sb.append("一等奖(5000000元)");
					correct++;
					break;
				}
			} else {
				switch (redHit) {
				case 4:
					sb.append("五等奖(10元)");
					correct++;
					break;
				case 5:
					sb.append("四等奖(200元)");
					correct++;
					break;
				case 6:
					sb.append("二等奖(当期高等奖奖金的30%)");
					correct++;
					break;
				}
			}
			logger.info(sb.toString());
		}
		// Correct rate
		logger.info("正确率：" + StringUtil.correctRate(correct, size));
	}

	/**
	 * 
	 * 五期内选红 命中个数
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int lastFivePhaseForRed(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 5) {
			return hit;
		}

		if (null != current) {
			Set<String> redCandidate = new HashSet<String>();
			// 近五期内列表
			for (int j = 1; j <= 5; j++) {
				Integer phase = list.get(index - j);
				DoubleColor ssq = map.get(phase);
				if (null != ssq) {
					List<String> ssqList = ssq.getRed();
					for (String red : ssqList) {
						redCandidate.add(red);
					}
				} // end of if(null != ssq)
			} // end of for

			// 获取当前红球列表
			List<String> redList = current.getRed();
			for (String red : redList) {
				if (redCandidate.contains(red)) {
					hit++;
				}
			} // end of for (String red : redList)
		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 下移号 命中个数
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int downForRed(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 1) {
			return hit;
		}

		if (null != current) {
			Set<String> currentRedSet = new HashSet<String>();
			// 获取当前红球列表
			List<String> redList = current.getRed();
			for (String red : redList) {
				currentRedSet.add(red);
			} // end of for (String red : redList)
			Integer lastPhase = list.get(index - 1);
			// 获取上期数据
			DoubleColor last = map.get(lastPhase);
			if (null != last) {
				Set<String> lastRedSet = new HashSet<String>();
				// 获取上期红球列表
				List<String> lastRedList = last.getRed();
				for (String red : lastRedList) {
					lastRedSet.add(red);
				} // end of for (String red : lastRedList)

				if (currentRedSet.retainAll(lastRedSet)) {
					hit = currentRedSet.size();
				}
			}

		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 篮球杀红 命中个数
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int lastBlueKillRed(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 1) {
			return hit;
		}

		if (null != current) {
			Set<String> currentRedSet = new HashSet<String>();
			// 获取当前红球列表
			List<String> redList = current.getRed();
			for (String red : redList) {
				currentRedSet.add(red);
			} // end of for (String red : redList)
			Integer lastPhase = list.get(index - 1);
			// 获取上期数据
			DoubleColor last = map.get(lastPhase);
			if (null != last && !currentRedSet.contains(last.getBlue())) {
				hit++;
			}
		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 上期红球最大号码减去最小号码杀红球命中
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int lastMaxMinusMinKillRed(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 1) {
			return hit;
		}

		if (null != current) {
			Set<String> currentRedSet = new HashSet<String>();
			// 获取当前红球列表
			List<String> redList = current.getRed();
			for (String red : redList) {
				currentRedSet.add(red);
			} // end of for (String red : redList)
			Integer lastPhase = list.get(index - 1);
			// 获取上期数据
			DoubleColor last = map.get(lastPhase);
			if (null != last) {
				List<String> lastRed = last.getRed();
				int result = Integer.valueOf(lastRed.get(5))
						- Integer.valueOf(lastRed.get(0));
				if (!currentRedSet.contains(StringUtil.getBallValue(result))) {
					hit++;
				}
			}
		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 上期红球杀蓝命中数
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int lastRedKillBlue(int index, List<Integer> list,
			Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 1) {
			return hit;
		}

		// 获取期号
		Integer currentPhase = list.get(index);
		// 获取本期数据
		DoubleColor current = map.get(currentPhase);
		if (null != current) {
			String currentBlue = current.getBlue();

			Integer lastPhase = list.get(index - 1);
			// 获取上期数据
			DoubleColor last = map.get(lastPhase);
			if (null != last) {
				List<String> lastRed = last.getRed();
				Set<String> set = new HashSet<String>();
				for (String lred : lastRed) {
					if (Integer.valueOf(lred) <= 16) {
						set.add(lred);
					}
				}

				if (!set.contains(currentBlue)) {
					hit++;
				}
			}
		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 期号杀蓝 命中数
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int phaseNumberKillBlue(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (null != current) {
			String currentBlue = current.getBlue();

			String phase = String.valueOf(current.getPhase());
			if (!phase.substring(6).equals(currentBlue.substring(1))) {
				hit++;
			}
		} // end of if(null != current)

		return hit;
	}

	/**
	 * 
	 * 加减法杀蓝 命中
	 * 
	 * @param index
	 *            期号列表的索引号
	 * @param current
	 *            本期双色球信息
	 * @param list
	 *            期号列表
	 * @param map
	 *            期号，内容键值对
	 * @return
	 */
	public static int addSubtractionKillBlue(int index, DoubleColor current,
			List<Integer> list, Map<Integer, DoubleColor> map) {
		int hit = 0;

		if (index < 2) {
			return hit;
		}

		if (null != current) {
			String currentBlue = current.getBlue();

			Set<String> set = new HashSet<String>();

			Integer lastPhaseOne = list.get(index - 1);
			DoubleColor lastOne = map.get(lastPhaseOne);
			int lastBlueOne = Integer.valueOf(lastOne.getBlue());

			Integer lastPhaseTwo = list.get(index - 2);
			DoubleColor lastTwo = map.get(lastPhaseTwo);
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

			if (!set.contains(currentBlue)) {
				hit++;
			}
		} // end of if(null != current)

		return hit;
	}

}
