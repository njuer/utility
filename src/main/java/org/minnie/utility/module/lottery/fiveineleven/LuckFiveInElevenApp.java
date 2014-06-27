package org.minnie.utility.module.lottery.fiveineleven;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.entity.lottery.FiveInElevenCandidate;
import org.minnie.utility.entity.lottery.FiveInElevenPredict;
import org.minnie.utility.entity.lottery.ShiShiCaiPredict;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;

public class LuckFiveInElevenApp {


	private static Logger logger = Logger.getLogger(LuckFiveInElevenApp.class
			.getName());

//	private static ResourceBundle rb;
//	private static BufferedInputStream inputStream;
	
	public static void main(String[] args) {
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);
		
		HttpSimulation hs = new HttpSimulation();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	
		/**
		 * 分析
		 */
//		List<FiveInElevenPredict> list = analyzeLuckFiveInEleven(hs, nvps, Constant.HLJ11XUAN5_ANY_FIVE, 100, true);
//		StringBuffer sb = new StringBuffer();
//		for(FiveInElevenPredict fiea : list){
//			sb.append("[").append(fiea.getPeriod()).append("]期结果：").append(fiea.getResult().toString()).append("\t");
//			int [] kill = fiea.getKill();
//			int count = 0;
//			int good = 0;
//			for(int i = 1 ; i <= 11; i++){
//				int k = kill[i];
//				if(k == 0){
//					continue;
//				}
//				if(k < 0){
//					good++;
//				}
//				count++;
//				sb.append(i).append("(").append(k).append(") ");
//			}
//			sb.append("[").append(fiea.getHit()).append("]\t").append(good).append("/").append(count).append("\n");
//		}
//		logger.info(sb.toString());
		
		/**
		 * 预测
		 */
		FiveInElevenCandidate fiec = getLuckFiveInElevenCandidate(hs, nvps, Constant.HLJ11XUAN5_ANY_FIVE, 20, false);
		StringBuffer sbc = new StringBuffer();
		sbc.append("[").append(fiec.getPeriod()).append("]预测：\t");
		int [] candidate = fiec.getCandidate();
		int count = 0;
		for (int i = 1; i <= 11; i++) {
//			int k = candidate[i];
			if (candidate[i] != 0) {
				count++;
				sbc.append(i).append("(").append(candidate[i]).append(") ");
			}
		}
		sbc.append("共").append(count).append("个");
		logger.info(sbc.toString());
	}
	
	/**
	 * 按位、记录数预测结果[杀号方式]
	 * 
	 * @param hs
	 *            网页内容处理类实例
	 * @param nvps
	 *            模拟请求参数
	 * @param wei
	 *            位：个(gewei)、十(shiwei)、百(baiwei)、千(qianwei)、万(wanwei)
	 * @param records
	 *            记录数，网易只支持20、30、50、100
	 * @param onAccuracy
	 *            是否参照“准确率”
	 */
	public static List<FiveInElevenPredict> analyzeLuckFiveInEleven(HttpSimulation hs,
			List<NameValuePair> nvps, String category, int records, Boolean onAccuracy) {
		String uriPath = "/shahao/hlj11x5/";
		uriPath += category + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(Constant.HOST_NETEASE_LOTTERY, uriPath, nvps);
//		return JsoupHtmlParser.analyzeLuckFiveInEleven(category, resp, onAccuracy);
		return JsoupHtmlParser.getNeteaseLuckFiveInEleven(resp);
	}
	
	public static FiveInElevenCandidate getLuckFiveInElevenCandidate(HttpSimulation hs,
			List<NameValuePair> nvps, String category, int records, Boolean onAccuracy) {
		String uriPath = "/shahao/hlj11x5/";
		uriPath += category + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(Constant.HOST_NETEASE_LOTTERY, uriPath, nvps);
//		return JsoupHtmlParser.analyzeLuckFiveInEleven(category, resp, onAccuracy);
		return JsoupHtmlParser.getLuckFiveInElevenCandidate(category, resp, onAccuracy);
	}

}
