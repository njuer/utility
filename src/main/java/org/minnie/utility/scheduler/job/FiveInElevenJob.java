package org.minnie.utility.scheduler.job;

import java.util.List;

import javax.mail.Session;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.minnie.utility.entity.lottery.FiveInElevenCandidate;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FiveInElevenJob implements Job {

	private static Logger logger = Logger.getLogger(FiveInElevenJob.class
			.getName());

	public static final String PARAM_HTTPSIMULATION = "hs";
	public static final String PARAM_NVPS = "nvps";
	public static final String PARAM_CATEGORY = "category";
	public static final String PARAM_MAIL_SESSION = "session";
	public static final String PARAM_MAIL_HOST = "host";
	public static final String PARAM_MAIL_USER = "user";
	public static final String PARAM_MAIL_PWD = "pwd";
	public static final String PARAM_MAIL_TO = "to";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap data = context.getMergedJobDataMap();
		HttpSimulation hs = (HttpSimulation) data.get(PARAM_HTTPSIMULATION);
		List<NameValuePair> nvps = (List<NameValuePair>) data.get(PARAM_NVPS);
		String category = data.getString(PARAM_CATEGORY);
		Session session = (Session) data.get(PARAM_MAIL_SESSION);
		String host = data.getString(PARAM_MAIL_HOST);
		String user = data.getString(PARAM_MAIL_USER);
		String pwd = data.getString(PARAM_MAIL_PWD);
		String[] to = (String[]) data.get(PARAM_MAIL_TO);

		/**
		 * 预测
		 */
		FiveInElevenCandidate fiec = getLuckFiveInElevenCandidate(hs, nvps,
				Constant.HLJ11XUAN5_ANY_FIVE, 20, false);

		int period = fiec.getPeriod();
		StringBuffer sbc = new StringBuffer();
		sbc.append("[").append(period).append("]预测：\t");
		int[] candidate = fiec.getCandidate();
		int count = 0;
		for (int i = 1; i <= 11; i++) {
			// int k = candidate[i];
			if (candidate[i] != 0) {
				count++;
				sbc.append(i).append("(").append(candidate[i]).append(") ");
			}
		}
		sbc.append("共").append(count).append("个");
		logger.info(sbc.toString());

		MailUtil.sendMail(session, host, user, pwd, to, period + "期("
				+ category + ")预测", sbc.toString());

	}

	public FiveInElevenCandidate getLuckFiveInElevenCandidate(
			HttpSimulation hs, List<NameValuePair> nvps, String category,
			int records, Boolean onAccuracy) {
		String uriPath = "/shahao/hlj11x5/";
		uriPath += category + "_" + records + ".html";

		String resp = hs.getResponseBodyByGet(Constant.HOST_NETEASE_LOTTERY,
				uriPath, nvps);

		return JsoupHtmlParser.getLuckFiveInElevenCandidate(category, resp,
				onAccuracy);
	}

}
