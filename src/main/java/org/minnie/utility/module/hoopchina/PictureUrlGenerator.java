package org.minnie.utility.module.hoopchina;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.minnie.utility.util.HtmlUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-7
 * 类说明
 */
public class PictureUrlGenerator implements Runnable {
	
	private static Logger logger = Logger.getLogger(PictureUrlGenerator.class.getName());

	
	private ExecutorService executorService;
	private BlockingQueue<HoopChina> pageUrlQueue;
	private BlockingQueue<HoopChina> pictureUrlQueue;


	public PictureUrlGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PictureUrlGenerator(ExecutorService executorService,
			BlockingQueue<HoopChina> pageUrlQueue) {
		super();
		this.executorService = executorService;
		this.pageUrlQueue = pageUrlQueue;
	}

	public PictureUrlGenerator(ExecutorService executorService,
			BlockingQueue<HoopChina> pageUrlQueue,
			BlockingQueue<HoopChina> pictureUrlQueue) {
		super();
		this.executorService = executorService;
		this.pageUrlQueue = pageUrlQueue;
		this.pictureUrlQueue = pictureUrlQueue;
	}



	public ExecutorService getExecutorService() {
		return executorService;
	}



	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}


	public BlockingQueue<HoopChina> getPageUrlQueue() {
		return pageUrlQueue;
	}



	public void setPageUrlQueue(BlockingQueue<HoopChina> pageUrlQueue) {
		this.pageUrlQueue = pageUrlQueue;
	}



	public BlockingQueue<HoopChina> getPictureUrlQueue() {
		return pictureUrlQueue;
	}



	public void setPictureUrlQueue(BlockingQueue<HoopChina> pictureUrlQueue) {
		this.pictureUrlQueue = pictureUrlQueue;
	}



	@Override
	public void run() {
//		HoopChina hoopChina;
//		try {
//			while(null != (hoopChina = pageUrlQueue.take())){
//				logger.info(Thread.currentThread() + " : " + hoopChina.toString());
//				String pictureURL = HtmlUtil.getPictureUrl(hoopChina.getPageUrl(), "id","bigpicpic");
//				hoopChina.setPirctureUrl(pictureURL);
////				logger.info(Thread.currentThread() + " pictureURL: " + pictureURL);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		while(!pageUrlQueue.isEmpty()){
			HoopChina hoopChina;
			try {
				hoopChina = pageUrlQueue.take();
//				logger.info(Thread.currentThread() + " : " + hoopChina.toString());
				String pictureURL = HtmlUtil.getPictureUrl(hoopChina.getPageUrl(), "id","bigpicpic");
				hoopChina.setPirctureUrl(pictureURL);
				pictureUrlQueue.put(hoopChina);
//				logger.info(Thread.currentThread() + " pictureURL: " + pictureURL);
			} catch (InterruptedException e) {
				logger.error(Thread.currentThread() + "----InterruptedException[PictureUrlGenerator->run]: "
						+ e.getMessage());
			}
		}
	}

}
