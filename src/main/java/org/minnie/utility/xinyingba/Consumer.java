package org.minnie.utility.xinyingba;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

public class Consumer implements Runnable {
	
	private static Logger logger = Logger.getLogger(Consumer.class.getName());

	private BlockingQueue<Video> queue;
    private ExecutorService defaultExecutorService;
    private String directory;
    private int size;
	private Object lock;

	public Consumer() {
		super();
	}
	
	public Consumer(BlockingQueue<Video> queue) {
		this.queue = queue;
	}
	
	public Consumer(BlockingQueue<Video> queue, ExecutorService defaultExecutorService, String directory) {
		this.queue = queue;
		this.defaultExecutorService = defaultExecutorService;
		this.directory = directory;
	}
	
	public BlockingQueue<Video> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Video> queue) {
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
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			/**
			 * 队列不空，则取队首元素
			 */
			while(!queue.isEmpty()){
				Video video = queue.take();
				if(ImageUtil.save2File(directory, video)){
					synchronized (lock) {
						size++;
					}
				}
				logger.info(Thread.currentThread() + " :" + video.toString());
			}
			if(null != defaultExecutorService){
				defaultExecutorService.shutdownNow();
				logger.info(Thread.currentThread() + " :shutdown ExecutorService");
				logger.info(Thread.currentThread() + " : size = " + size);
			}

		} catch (InterruptedException e) {
			logger.error("InterruptedException[Consumer->run]: "+ e.getMessage());

		}

	}

}
