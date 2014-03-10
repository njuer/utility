package org.minnie.utility.module.netease;

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

	private BlockingQueue<Netease> pictureUrlQueue;
	private String directory;

	public PictureGenerator() {
		super();
	}
	
	public PictureGenerator(BlockingQueue<Netease> pictureUrlQueue,
			String directory) {
		super();
		this.pictureUrlQueue = pictureUrlQueue;
		this.directory = directory;
	}
	
	public BlockingQueue<Netease> getPictureUrlQueue() {
		return pictureUrlQueue;
	}

	public void setPictureUrlQueue(BlockingQueue<Netease> pictureUrlQueue) {
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
			try {
				Netease netease = pictureUrlQueue.take();
				if (null != netease) {
					boolean flag = ImageUtil.save2File(directory, netease);
					if (flag) {
						logger.info(Thread.currentThread() + ": "
								+ netease.getImg() + "[下载成功]");
					} else {
						logger.info(Thread.currentThread() + ": "
								+ netease.getImg() + "[下载失败]");
					}

				}
			} catch (InterruptedException e) {
				logger.error(Thread.currentThread() + "----InterruptedException[PictureGenerator->run]: "
						+ e.getMessage());
			}
		}
	}

}
