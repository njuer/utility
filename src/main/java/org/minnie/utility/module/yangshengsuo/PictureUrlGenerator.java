package org.minnie.utility.module.yangshengsuo;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-7
 * 类说明
 */
public class PictureUrlGenerator implements Runnable {
	
	private static Logger logger = Logger.getLogger(PictureUrlGenerator.class.getName());

	private BlockingQueue<Regimen> pageUrlQueue;
	private BlockingQueue<Regimen> pictureUrlQueue;

	public PictureUrlGenerator() {
		super();
	}
	
	public PictureUrlGenerator(BlockingQueue<Regimen> pageUrlQueue,
			BlockingQueue<Regimen> pictureUrlQueue) {
		super();
		this.pageUrlQueue = pageUrlQueue;
		this.pictureUrlQueue = pictureUrlQueue;
	}

	public BlockingQueue<Regimen> getPageUrlQueue() {
		return pageUrlQueue;
	}

	public void setPageUrlQueue(BlockingQueue<Regimen> pageUrlQueue) {
		this.pageUrlQueue = pageUrlQueue;
	}

	public BlockingQueue<Regimen> getPictureUrlQueue() {
		return pictureUrlQueue;
	}

	public void setPictureUrlQueue(BlockingQueue<Regimen> pictureUrlQueue) {
		this.pictureUrlQueue = pictureUrlQueue;
	}

	@Override
	public void run() {
		while(!pageUrlQueue.isEmpty()){
			Regimen regimen;
			try {
				regimen = pageUrlQueue.take();
//				logger.info(Thread.currentThread() + " : " + regimen.toString());
				String pictureUrl = JsoupHtmlParser.getRegimenPictureUrl(regimen);
				if(pictureUrl.startsWith("/")){
					pictureUrl = Constant.URL_TIANTIANYULE + pictureUrl;
				}
				regimen.setPictureUrl(pictureUrl);
				pictureUrlQueue.put(regimen);
				logger.info(Thread.currentThread() + " pictureUrl: " + pictureUrl);
			} catch (InterruptedException e) {
				logger.error(Thread.currentThread() + "----InterruptedException[PictureUrlGenerator->run]: "
						+ e.getMessage());
			}
		}
	}

}
