package org.minnie.utility.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class ImageUtil {
	
	private static Logger logger = Logger.getLogger(ImageUtil.class.getName());

	/**
	 * 获取图片的输入流
	 * @param imageSource	图片url
	 * @return
	 */
	public static InputStream getInputStream(String imageSource) {
		URL url;
		InputStream is = null;
		try {
			url = new URL(imageSource);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			is = con.getInputStream();
			
			 is.close();
		} catch (MalformedURLException e) {
//			e.printStackTrace();
			logger.error("MalformedURLException[ImageUtil->getInputStream]: " + e.getMessage());
		} catch (IOException e) {
//			e.printStackTrace();
			logger.error("IOException[ImageUtil->getInputStream]: " + e.getMessage());
		}

		return is;
	}
}
