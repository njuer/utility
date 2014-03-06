package org.minnie.utility.module.hoopchina;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

public class ConsumerV2 implements Runnable {
	
	private static Logger logger = Logger.getLogger(ConsumerV2.class.getName());

	private BlockingQueue<HoopChina> queue;
    private ExecutorService executorService;
    private String directory;

	public ConsumerV2() {
		super();
	}
	
	public ConsumerV2(BlockingQueue<HoopChina> queue) {
		this.queue = queue;
	}
	
	public ConsumerV2(BlockingQueue<HoopChina> queue, ExecutorService executorService) {
		this.queue = queue;
		this.executorService = executorService;
	}
	
	public ConsumerV2(BlockingQueue<HoopChina> queue, ExecutorService executorService, String directory) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.directory = directory;
	}

	public BlockingQueue<HoopChina> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<HoopChina> queue) {
		this.queue = queue;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	@Override
	public void run() {
		try {
			/**
			 * 队列不空，则取队首元素
			 */
			while(true){
				HoopChina hoopChina = this.queue.take();
				ImageUtil.save2File(this.directory, hoopChina);
				logger.info(Thread.currentThread() + " :" + hoopChina.getPirctureUrl());
			}


		} catch (InterruptedException e) {
			logger.error("InterruptedException[Consumer->run]: "+ e.getMessage());

		}

	}

}
