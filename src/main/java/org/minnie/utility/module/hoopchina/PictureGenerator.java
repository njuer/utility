package org.minnie.utility.module.hoopchina;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-7 类说明
 */
public class PictureGenerator implements Runnable {

	private static Logger logger = Logger.getLogger(PictureGenerator.class
			.getName());

	private ExecutorService executorService;
	private BlockingQueue<HoopChina> pictureUrlQueue;
	private AtomicInteger atomic;
	private String directory;
	private int total;

	public PictureGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PictureGenerator(ExecutorService executorService,
			BlockingQueue<HoopChina> pictureUrlQueue) {
		super();
		this.executorService = executorService;
		this.pictureUrlQueue = pictureUrlQueue;
	}

	public PictureGenerator(ExecutorService executorService,
			BlockingQueue<HoopChina> pictureUrlQueue, String directory) {
		super();
		this.executorService = executorService;
		this.pictureUrlQueue = pictureUrlQueue;
		this.directory = directory;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public BlockingQueue<HoopChina> getPictureUrlQueue() {
		return pictureUrlQueue;
	}

	public void setPictureUrlQueue(BlockingQueue<HoopChina> pictureUrlQueue) {
		this.pictureUrlQueue = pictureUrlQueue;
	}

	public AtomicInteger getAtomic() {
		return atomic;
	}

	public void setAtomic(AtomicInteger atomic) {
		this.atomic = atomic;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public void run() {
		while (true) {
//			logger.info(Thread.currentThread() + ": total="+total);
//			if (atomic.incrementAndGet() >= total) {
//				logger.info(Thread.currentThread() + ": 跳出atomic="+atomic.intValue() + ":total="+total);
//				break;
//			}
			try {
				HoopChina hoopChina = pictureUrlQueue.take();
				if (null != hoopChina) {
					boolean flag = ImageUtil.save2File(directory, hoopChina);
					if (flag) {
						logger.info(Thread.currentThread() + ": "
								+ hoopChina.getPirctureUrl() + "[下载成功]");
					} else {
						logger.info(Thread.currentThread() + ": "
								+ hoopChina.getPirctureUrl() + "[下载失败]");
					}

				}
			} catch (InterruptedException e) {
				logger.error(Thread.currentThread() + "----InterruptedException[PictureGenerator->run]: "
						+ e.getMessage());
			}
		}
	}

}
