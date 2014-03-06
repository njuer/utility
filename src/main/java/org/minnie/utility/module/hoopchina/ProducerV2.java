package org.minnie.utility.module.hoopchina;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.minnie.utility.util.HtmlUtil;

public class ProducerV2 implements Runnable {

	private static Logger logger = Logger.getLogger(ProducerV2.class.getName());

	private BlockingQueue<HoopChina> queue;
	private ExecutorService executorService;
	private String homeURL;
	private AtomicInteger atomic;
	private HoopChina hoopChina;

	public ProducerV2() {
		super();
	}

	public ProducerV2(BlockingQueue<HoopChina> queue) {
		this.queue = queue;
	}

	public ProducerV2(BlockingQueue<HoopChina> queue,
			ExecutorService executorService) {
		super();
		this.queue = queue;
		this.executorService = executorService;
	}

	public ProducerV2(BlockingQueue<HoopChina> queue,
			ExecutorService executorService, String homeURL) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
	}

	public ProducerV2(BlockingQueue<HoopChina> queue,
			ExecutorService executorService, String homeURL,
			AtomicInteger atomic) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
		this.atomic = atomic;
	}
	
	public ProducerV2(BlockingQueue<HoopChina> queue,
			ExecutorService executorService, String homeURL,
			AtomicInteger atomic, HoopChina hoopChina) {
		super();
		this.queue = queue;
		this.executorService = executorService;
		this.homeURL = homeURL;
		this.atomic = atomic;
		this.hoopChina = hoopChina;
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

	public String getHomeURL() {
		return homeURL;
	}

	public void setHomeURL(String homeURL) {
		this.homeURL = homeURL;
	}

	public AtomicInteger getAtomic() {
		return atomic;
	}

	public void setAtomic(AtomicInteger atomic) {
		this.atomic = atomic;
	}

	public HoopChina getHoopChina() {
		return hoopChina;
	}

	public void setHoopChina(HoopChina hoopChina) {
		this.hoopChina = hoopChina;
	}

	@Override
	public void run() {
		try {
			while(this.atomic.intValue() < this.hoopChina.getTotal()){
				int index = this.homeURL.lastIndexOf(".");
				int count = atomic.incrementAndGet();
				String pageURL = this.homeURL.substring(0, index) + "-" + count
						+ ".html";
				String pictureURL = HtmlUtil.getPictureUrl(pageURL, "id",
						"bigpicpic");

				this.hoopChina.setPirctureUrl(pictureURL);
				
				queue.put(this.hoopChina);
				logger.info(Thread.currentThread() + " ==[count = " + count
						+ "]获取URL:" + pictureURL);
			}
		} catch (InterruptedException e) {
			logger.error(Thread.currentThread() + "----InterruptedException[Producer->run]: "
					+ e.getMessage());
		}
	}

}
