package org.minnie.utility.module.hoopchina;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.minnie.utility.util.HtmlUtil;

public class Producer implements Runnable {

	private static Logger logger = Logger.getLogger(Producer.class.getName());

	private BlockingQueue<String> queue;
	private ExecutorService executorService;
	private String homeURL;
	// private Object lock;
	// private int count;
	private AtomicInteger atomic;
	private int total;

	public Producer() {
		super();
	}

	public Producer(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public Producer(BlockingQueue<String> queue,
			ExecutorService executorService) {
		super();
		this.queue = queue;
		this.executorService = executorService;
	}

	public Producer(BlockingQueue<String> queue,
			ExecutorService executorService, String homeURL) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
	}

	public Producer(BlockingQueue<String> queue,
			ExecutorService executorService, String homeURL,
			AtomicInteger atomic) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
		this.atomic = atomic;
	}
	
	public Producer(BlockingQueue<String> queue,
			ExecutorService executorService, String homeURL,
			AtomicInteger atomic, int total) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
		this.atomic = atomic;
		this.total = total;
	}

	public BlockingQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public String getHomeURL() {
		return homeURL;
	}

	public void setHomeURL(String homeURL) {
		this.homeURL = homeURL;
	}

	// public Object getLock() {
	// return lock;
	// }
	//
	// public void setLock(Object lock) {
	// this.lock = lock;
	// }
	//
	// public int getCount() {
	// return count;
	// }
	//
	// public void setCount(int count) {
	// this.count = count;
	// }

	public AtomicInteger getAtomic() {
		return atomic;
	}

	public void setAtomic(AtomicInteger atomic) {
		this.atomic = atomic;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public void run() {
		try {
			// synchronized (lock) {
			// int index = url.lastIndexOf(".");
			// String pageUrl = url.substring(0, index) + "-" + count + ".html";
			// String url = HtmlUtil.getPictureUrl(pageUrl, "id", "bigpicpic");
			// count++;
			// }
			// atomic.incrementAndGet();
			while(this.atomic.intValue() < this.total){
				int index = this.homeURL.lastIndexOf(".");
				int count = atomic.incrementAndGet();
				String pageURL = this.homeURL.substring(0, index) + "-" + count
						+ ".html";
				String pictureURL = HtmlUtil.getPictureUrl(pageURL, "id",
						"bigpicpic");

				queue.put(pictureURL);
				logger.info(Thread.currentThread() + " ==[count = " + count
						+ "]获取URL:" + pictureURL);
			}
//			if(this.atomic.intValue() == this.total && null != this.defaultExecutorService){
//				this.defaultExecutorService.shutdownNow();
//				logger.info(Thread.currentThread() + " :shutdown ExecutorService");
//			}
		} catch (InterruptedException e) {
			logger.error(Thread.currentThread() + "----InterruptedException[Producer->run]: "
					+ e.getMessage());
		}
	}

}
