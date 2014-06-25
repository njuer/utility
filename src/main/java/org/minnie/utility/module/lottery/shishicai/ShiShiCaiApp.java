package org.minnie.utility.module.lottery.shishicai;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;

/**
 * @author neiplzer@gmail.com
 * @version 2014-6-24 类说明
 */
public class ShiShiCaiApp {

	// public static final String JXSSC_GEWEI_20 =
	// "http://zx.caipiao.163.com/shahao/jxssc/gewei_20.html";
	// public static final String JXSSC_GEWEI_30 =
	// "http://zx.caipiao.163.com/shahao/jxssc/gewei_30.html";
	// public static final String JXSSC_GEWEI_50 =
	// "http://zx.caipiao.163.com/shahao/jxssc/gewei_50.html";
	// public static final String JXSSC_GEWEI_100 =
	// "http://zx.caipiao.163.com/shahao/jxssc/gewei_100.html";

	private static Logger logger = Logger.getLogger(ShiShiCaiApp.class
			.getName());

	public static final String HOST_NETEASE = "zx.caipiao.163.com";

	public static void main(String[] args) {

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);

		HttpSimulation hs = new HttpSimulation();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		logger.info("============(有概率)============");
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_GEWEI, 20, true);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_SHIWEI, 20, true);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_BAIWEI, 20, true);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_QIANWEI, 20, true);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_WANWEI, 20, true);

		logger.info("============(无概率)============");
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_GEWEI, 20, false);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_SHIWEI, 20, false);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_BAIWEI, 20, false);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_QIANWEI, 20, false);
		analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_WANWEI, 20, false);
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
	public static void analyzeXinShiShiCai(HttpSimulation hs,
			List<NameValuePair> nvps, String wei, int records,
			Boolean onAccuracy) {
		String uriPath = "/shahao/jxssc/";
		uriPath += wei + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(HOST_NETEASE, uriPath, nvps);
		JsoupHtmlParser.analyzeXinShiShiCai(wei, resp, onAccuracy);
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
	 */
	public static void analyzeXinShiShiCai(HttpSimulation hs,
			List<NameValuePair> nvps, String wei, int records) {
		String uriPath = "/shahao/jxssc/";
		uriPath += wei + "_" + records + ".html";

		String weiCn = "";
		if (Constant.JXSSC_GEWEI.equals(wei)) {
			weiCn = "个位";
		} else if (Constant.JXSSC_SHIWEI.equals(wei)) {
			weiCn = "十位";
		} else if (Constant.JXSSC_BAIWEI.equals(wei)) {
			weiCn = "百位";
		} else if (Constant.JXSSC_QIANWEI.equals(wei)) {
			weiCn = "千位";
		} else if (Constant.JXSSC_WANWEI.equals(wei)) {
			weiCn = "万位";
		}

		String resp = hs.getResponseBodyByGet(HOST_NETEASE, uriPath, nvps);
		logger.info("============"+weiCn+"(无概率)============");
		JsoupHtmlParser.analyzeXinShiShiCai(wei, resp, false);
		logger.info("============"+weiCn+"(有概率)============");
		JsoupHtmlParser.analyzeXinShiShiCai(wei, resp, true);
	}
}
