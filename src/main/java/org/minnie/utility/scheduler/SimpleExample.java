package org.minnie.utility.scheduler;

import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.util.Constant;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleExample {

	public static void main(String[] args) {
		
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);

		// Quartz 1.6.3
        //JobDetail job = new JobDetail();
        //job.setName("dummyJobName");
        //job.setJobClass(HelloJob.class);     
 
        JobDetail job = JobBuilder.newJob(HelloJob.class)
        .withIdentity("dummyJobName", "group1").build();
 
        // Quartz 1.6.3
        //CronTrigger trigger = new CronTrigger();
        //trigger.setName("dummyTriggerName");
        //trigger.setCronExpression("0/5 * * * * ?");
 
        Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("dummyTriggerName", "group1")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 7/10 8-22 * * ?"))
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
