package org.minnie.utility.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

	private static Logger logger = Logger.getLogger(HelloJob.class.getName());
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// Say Hello to the World and display the date/time
		logger.info("Hello World! - " + new Date());
	}

}
