package org.minnie.utility.xinyingba;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

public class Consumer implements Runnable {
	
	private static Logger logger = Logger.getLogger(Consumer.class.getName());

	private BlockingQueue<Video> queue;
    private ExecutorService defaultExecutorService;


	public Consumer(BlockingQueue<Video> queue) {
		this.queue = queue;
	}
	
	public Consumer(BlockingQueue<Video> queue, ExecutorService defaultExecutorService) {
		this.queue = queue;
		this.defaultExecutorService = defaultExecutorService;
	}

	@Override
	public void run() {
		try {
			/**
			 * 队列不空，则取队首元素
			 */
			while(!queue.isEmpty()){
				Video video = queue.take();
				ImageUtil.save2File(video);
				logger.info(Thread.currentThread() + " :" + video.toString());
			}
			if(null != defaultExecutorService){
				defaultExecutorService.shutdownNow();
				logger.info(Thread.currentThread() + " :shutdown ExecutorService");
			}

		} catch (InterruptedException e) {
			logger.error("InterruptedException[Consumer->run]: "+ e.getMessage());

		}

	}

}
