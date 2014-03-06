package org.minnie.utility.xinyingba;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

public class Producer implements Runnable {

	private static Logger logger = Logger.getLogger(Producer.class.getName());
	
	private BlockingQueue<Video> queue;
    private ExecutorService defaultExecutorService;


	public Producer() {
		super();
	}
	
	public Producer(BlockingQueue<Video> queue) {
		this.queue = queue;
	}
	

	public Producer(BlockingQueue<Video> queue,
			ExecutorService defaultExecutorService) {
		super();
		this.queue = queue;
		this.defaultExecutorService = defaultExecutorService;
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

	@Override
	public void run() {
		String uuid = UUID.randomUUID().toString();
		try {
			Video video = new Video();
			video.setUuid(uuid);
			queue.put(video);
			logger.info(Thread.currentThread() + " produce uuid:" + uuid);
		} catch (InterruptedException e) {
			logger.error("InterruptedException[Producer->run]: "+ e.getMessage());
		}
	}

}
