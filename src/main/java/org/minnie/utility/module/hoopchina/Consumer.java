package org.minnie.utility.module.hoopchina;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

public class Consumer implements Runnable {
	
	private static Logger logger = Logger.getLogger(Consumer.class.getName());

	private BlockingQueue<String> queue;
    private ExecutorService defaultExecutorService;
    private String directory;
    private String folder;

	public Consumer() {
		super();
	}
	
	public Consumer(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	public Consumer(BlockingQueue<String> queue, ExecutorService defaultExecutorService) {
		this.queue = queue;
		this.defaultExecutorService = defaultExecutorService;
	}
	
	public Consumer(BlockingQueue<String> queue, ExecutorService defaultExecutorService, String directory, String folder) {
		super();
		this.queue = queue;
		this.defaultExecutorService = defaultExecutorService;
		this.directory = directory;
		this.folder = folder;
	}

	public BlockingQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public ExecutorService getDefaultExecutorService() {
		return defaultExecutorService;
	}

	public void setDefaultExecutorService(ExecutorService defaultExecutorService) {
		this.defaultExecutorService = defaultExecutorService;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	@Override
	public void run() {
		try {
			/**
			 * 队列不空，则取队首元素
			 */
			while(!this.queue.isEmpty()){
				String imageSource = this.queue.take();
				ImageUtil.save2File(this.directory, this.folder, imageSource);
				logger.info(Thread.currentThread() + " :" + imageSource);
			}


		} catch (InterruptedException e) {
			logger.error("InterruptedException[Consumer->run]: "+ e.getMessage());

		}

	}

}
