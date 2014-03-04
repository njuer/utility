package org.minnie.utility.xinyingba;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class Producer implements Runnable {

	private static Logger logger = Logger.getLogger(Producer.class.getName());
	
	private BlockingQueue<Video> queue;

	public Producer(BlockingQueue<Video> queue) {
		this.queue = queue;
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
