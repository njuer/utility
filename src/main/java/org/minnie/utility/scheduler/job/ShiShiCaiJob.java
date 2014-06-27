package org.minnie.utility.scheduler.job;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.minnie.utility.entity.lottery.ShiShiCaiPredict;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ShiShiCaiJob implements Job {

	
	private static Logger logger = Logger.getLogger(ShiShiCaiJob.class
			.getName());

	public static final String PARAM_HTTPSIMULATION = "hs";
	public static final String PARAM_NVPS = "nvps";
	public static final String PARAM_MAIL_SESSION = "session";
	public static final String PARAM_MAIL_HOST = "host";
	public static final String PARAM_MAIL_USER = "user";
	public static final String PARAM_MAIL_PWD = "pwd";
	public static final String PARAM_MAIL_TO = "to";


	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap data = context.getMergedJobDataMap();
		HttpSimulation hs = (HttpSimulation)data.get(PARAM_HTTPSIMULATION);
		List<NameValuePair> nvps = (List<NameValuePair>)data.get(PARAM_NVPS);
		Session session = (Session)data.get(PARAM_MAIL_SESSION);
		String host = data.getString(PARAM_MAIL_HOST);
		String user = data.getString(PARAM_MAIL_USER);
		String pwd = data.getString(PARAM_MAIL_PWD);
		String [] to = (String [])data.get(PARAM_MAIL_TO);

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

		sb.append(new Date() + "============(无概率)============").append("\n");
		sb.append("[").append(period).append("]个位").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★");
		}
		sb.append("\n");
		if(ssc.getOdd() > 6){
			flag = true;
			sb.append("\t单(").append(ssc.getOdd()).append(")").append("\n");
		}
		if(ssc.getEven() > 6){
			flag = true;
			sb.append("\t双(").append(ssc.getEven()).append(")").append("\n");
		}
		if(ssc.getBig() > 6){
			flag = true;
			sb.append("\t大(").append(ssc.getBig()).append(")").append("\n");
		}
		if(ssc.getSmall() > 6){
			flag = true;
			sb.append("\t小(").append(ssc.getSmall()).append(")").append("\n");
		}
		
		/**
		 * 十位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_SHIWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]十位").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★");
		}
		sb.append("\n");

		if(ssc.getOdd() > 6){
			flag = true;
			sb.append("\t单(").append(ssc.getOdd()).append(")").append("\n");
		}
		if(ssc.getEven() > 6){
			flag = true;
			sb.append("\t双(").append(ssc.getEven()).append(")").append("\n");
		}
		if(ssc.getBig() > 6){
			flag = true;
			sb.append("\t大(").append(ssc.getBig()).append(")").append("\n");
		}
		if(ssc.getSmall() > 6){
			flag = true;
			sb.append("\t小(").append(ssc.getSmall()).append(")").append("\n");
		}
		
		/**
		 * 百位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_BAIWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]百位").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★");
		}
		sb.append("\n");
		
		/**
		 * 千位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_QIANWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]千位").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★");
		}
		sb.append("\n");

		/**
		 * 万位
		 */
		ssc = analyzeXinShiShiCai(hs, nvps, Constant.JXSSC_WANWEI, 20, false);
		predict.clear();
		predict = ssc.getPredict();

		sb.append("[").append(period).append("]万位").append(Arrays.toString(predict.toArray()));
		if(predict.size() <= 3){
			flag = true;
			sb.append("★");
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
	public ShiShiCaiPredict analyzeXinShiShiCai(HttpSimulation hs,
			List<NameValuePair> nvps, String wei, int records,
			Boolean onAccuracy) {
		String uriPath = "/shahao/jxssc/";
		uriPath += wei + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(Constant.HOST_NETEASE_LOTTERY, uriPath, nvps);
		return JsoupHtmlParser.analyzeXinShiShiCai(wei, resp, onAccuracy);
	}

}
