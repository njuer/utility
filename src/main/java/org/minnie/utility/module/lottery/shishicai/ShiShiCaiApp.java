package org.minnie.utility.module.lottery.shishicai;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.mail.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.entity.lottery.ShiShiCaiPredict;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.MailUtil;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-6-24 类说明
 */
public class ShiShiCaiApp {

	private static Logger logger = Logger.getLogger(ShiShiCaiApp.class
			.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;
	private static Session session;
	
	public static final String HOST_NETEASE = "zx.caipiao.163.com";

	public static void main(String[] args) {

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);

		String starttls = null;
		String host = null;
		String port = null;
		String auth = null;
		String user = null;
		String pwd = null;
		String receiver = null;
		List<String> receiverList = new ArrayList<String>();

		
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

			starttls = rb.getString("mail.smtp.starttls.enable");
			if (null == starttls
					|| StringUtils.isBlank(starttls)) {
				starttls = "true";
			}
			
			host = rb.getString("mail.smtp.host");
			if (null == host
					|| StringUtils.isBlank(host)) {
				host = "smtp.yeah.net";
			}
			
			port = rb.getString("mail.smtp.port");
			if (null == port
					|| StringUtils.isBlank(port)) {
				port = "25";
			}
			
			
			auth = rb.getString("mail.smtp.auth");
			if (null == auth
					|| StringUtils.isBlank(auth)) {
				auth = "true";
			}
			
			user = rb.getString("mail.smtp.user");
			
			pwd = rb.getString("mail.smtp.password");
			
			receiver = rb.getString("mail.smtp.receiver");
			if (null == receiver
					|| StringUtils.isBlank(receiver)) {
				receiverList.add("neiplz@yeah.net");
			} else {
				String [] receivers = receiver.split(";");
				for(String rec : receivers){
					if(StringUtil.isEmail(rec)){
						receiverList.add(rec);
					}
				}
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
		
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", user);
		props.put("mail.smtp.password", pwd);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", auth);

		session = Session.getDefaultInstance(props);
		
		String [] to = new String[receiverList.size()];
		receiverList.toArray(to);
		
		HttpSimulation hs = new HttpSimulation();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		List<ShiShiCaiPredict> list = new ArrayList<ShiShiCaiPredict>();
		

		/**
		 * ========================================================
		 * =======	                                    无概率						=======
		 * ========================================================
		 */
		Boolean flag = false;
		StringBuffer sb = new StringBuffer();
		/**
		 * 个位
		 */
		ShiShiCaiPredict ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_GEWEI, 20, false);
		List<Integer> predict = ssc.getPredict();
		Integer period = ssc.getPeriod();

		sb.append("============(无概率)============").append("\n");
		sb.append("[").append(period).append("]个位>>>>").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★★★★★");
		}
		sb.append("\n");
		if(ssc.getOdd() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]个位<<<<").append("\t单(").append(ssc.getOdd()).append(")").append("\n");
		}
		if(ssc.getEven() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]个位<<<<").append("\t双(").append(ssc.getEven()).append(")").append("\n");
		}
		if(ssc.getBig() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]个位<<<<").append("\t大(").append(ssc.getBig()).append(")").append("\n");
		}
		if(ssc.getSmall() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]个位<<<<").append("\t小(").append(ssc.getSmall()).append(")").append("\n");
		}
		
		/**
		 * 十位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_SHIWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]十位>>>>").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★★★★★");
		}
		sb.append("\n");

		if(ssc.getOdd() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]十位<<<<").append("\t单(").append(ssc.getOdd()).append(")").append("\n");
		}
		if(ssc.getEven() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]十位<<<<").append("\t双(").append(ssc.getEven()).append(")").append("\n");
		}
		if(ssc.getBig() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]十位<<<<").append("\t大(").append(ssc.getBig()).append(")").append("\n");
		}
		if(ssc.getSmall() > 6){
			flag = true;
			sb.append("\t[").append(period).append("]十位<<<<").append("\t小(").append(ssc.getSmall()).append(")").append("\n");
		}
		
		/**
		 * 百位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_BAIWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]百位>>>>").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★★★★★");
		}
		sb.append("\n");
		
		/**
		 * 千位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_QIANWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]千位>>>>").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★★★★★");
		}
		sb.append("\n");

		/**
		 * 万位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_WANWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]万位>>>>").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★★★★★");
		}
		sb.append("\n");
		String result = sb.toString();
		logger.info(result);
		
		if(flag){
			MailUtil.sendMail(session, host, user, pwd, to, period + "期(无概率)预测", result);
		}
		
		/**
		 * ========================================================
		 * =======	                                    有概率						=======
		 * ========================================================
		 */
//		logger.info("============(有概率)============");
//		list.clear();
//		list.add(analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_GEWEI, 20, true));
//		list.add(analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_SHIWEI, 20, true));
//		list.add(analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_BAIWEI, 20, true));
//		list.add(analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_QIANWEI, 20, true));
//		list.add(analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_WANWEI, 20, true));
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
	public static ShiShiCaiPredict analyzeXinShiShiCai(HttpSimulation hs,
			List<NameValuePair> nvps, String wei, int records,
			Boolean onAccuracy) {
		String uriPath = "/shahao/jxssc/";
		uriPath += wei + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(HOST_NETEASE, uriPath, nvps);
		return JsoupHtmlParser.analyzeXinShiShiCai(wei, resp, onAccuracy);
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
