package org.minnie.utility.scheduler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.mail.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.scheduler.job.NeteaseBbsJob;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.StringUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class NeteaseBbsScheduler {

	private static Logger logger = Logger.getLogger(NeteaseBbsScheduler.class
			.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;
	private static Session session;


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
			if (null == starttls || StringUtils.isBlank(starttls)) {
				starttls = "true";
			}

			host = rb.getString("mail.smtp.host");
			if (null == host || StringUtils.isBlank(host)) {
				host = "smtp.yeah.net";
			}

			port = rb.getString("mail.smtp.port");
			if (null == port || StringUtils.isBlank(port)) {
				port = "25";
			}

			auth = rb.getString("mail.smtp.auth");
			if (null == auth || StringUtils.isBlank(auth)) {
				auth = "true";
			}

			user = rb.getString("mail.smtp.user");

			pwd = rb.getString("mail.smtp.password");

			receiver = rb.getString("mail.smtp.receiver");
			if (null == receiver || StringUtils.isBlank(receiver)) {
				receiverList.add("neiplz@yeah.net");
			} else {
				String[] receivers = receiver.split(";");
				for (String rec : receivers) {
					if (StringUtil.isEmail(rec)) {
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

		String[] to = new String[receiverList.size()];
		receiverList.toArray(to);

		HttpSimulation hs = new HttpSimulation();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();


		JobDetail job = JobBuilder.newJob(NeteaseBbsJob.class)
				.withIdentity("jobBBS", "groupBBS").build();
		JobDataMap dataMap = job.getJobDataMap();
		dataMap.put(NeteaseBbsJob.PARAM_HTTPSIMULATION, hs);
		dataMap.put(NeteaseBbsJob.PARAM_NVPS, nvps);
		dataMap.put(NeteaseBbsJob.PARAM_MAIL_SESSION, session);
		dataMap.put(NeteaseBbsJob.PARAM_MAIL_HOST, host);
		dataMap.put(NeteaseBbsJob.PARAM_MAIL_USER, user);
		dataMap.put(NeteaseBbsJob.PARAM_MAIL_PWD, pwd);
		dataMap.put(NeteaseBbsJob.PARAM_MAIL_TO, to);
		
		//每天12-15点，每时段1秒开始执行，间隔为10分钟
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("triggerBBS", "groupBBS")
				.withSchedule(
						CronScheduleBuilder.cronSchedule("1 0/10 12-15 * * ?"))
				.build();

		// schedule it
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
