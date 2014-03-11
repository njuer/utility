package org.minnie.utility.module.yangshengsuo;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import org.minnie.utility.util.ImageUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-7 类说明
 */
public class PictureGenerator implements Runnable {

	private static Logger logger = Logger.getLogger(PictureGenerator.class
			.getName());

	private BlockingQueue<Regimen> pictureUrlQueue;
	private String directory;

	public PictureGenerator() {
		super();
	}

	public PictureGenerator(BlockingQueue<Regimen> pictureUrlQueue,
			String directory) {
		super();
		this.pictureUrlQueue = pictureUrlQueue;
		this.directory = directory;
	}

	public BlockingQueue<Regimen> getPictureUrlQueue() {
		return pictureUrlQueue;
	}

	public void setPictureUrlQueue(BlockingQueue<Regimen> pictureUrlQueue) {
		this.pictureUrlQueue = pictureUrlQueue;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
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
				Regimen regimen = pictureUrlQueue.take();
				if (null != regimen) {
					boolean flag = ImageUtil.save2File(directory, regimen);
					if (flag) {
						logger.info(Thread.currentThread() + ": "
								+ regimen.getPictureUrl() + "[下载成功]");
					} else {
						logger.info(Thread.currentThread() + ": "
								+ regimen.getPictureUrl() + "[下载失败]");
					}

				}
			} catch (InterruptedException e) {
				logger.error(Thread.currentThread() + "----InterruptedException[PictureGenerator->run]: "
						+ e.getMessage());
			}
		}
	}

}
