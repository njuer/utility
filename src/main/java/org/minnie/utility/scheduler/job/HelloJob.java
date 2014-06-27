package org.minnie.utility.scheduler.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

	private static Logger logger = Logger.getLogger(HelloJob.class.getName());

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Say Hello to the World and display the date/time
		String word = context.getMergedJobDataMap().getString("word");
		int centense = context.getMergedJobDataMap().getInt("sentence");

		logger.info("[" + word + "]Hello World! " + centense + "- "
				+ new Date());
	}

}
