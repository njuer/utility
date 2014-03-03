package org.minnie.utility.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ImageUtil {

	private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);


	public static InputStream getInputStream(String imageSource) {
		
		Matcher matcher = urlPattern.matcher(imageSource);
		if (matcher.find()) {
			imageSource = matcher.group();
		} else if(Constant.IMG_NO_PIC.equals(imageSource)){
			imageSource = Constant.URL_XINYINGBA + imageSource;
		} else {
			return null;
		}
		
		URL url;
		InputStream is = null;
		
		try {
			url = new URL(imageSource);

			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();

			// 设定timeout时间
			httpUrlConnection.setReadTimeout(30 * 1000);
			httpUrlConnection.setConnectTimeout(15 * 1000);
			
			httpUrlConnection.setDoInput(true);
			
//			httpUrlConnection.setRequestMethod("GET");
//			httpUrlConnection
//					.setRequestProperty(
//							"Accept",
//							"image/gif, image/jpeg, image/png, "
//									+ "application/x-shockwave-flash, application/xaml+xml, "
//									+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
//									+ "application/x-ms-application, application/vnd.ms-excel, "
//									+ "application/vnd.ms-powerpoint, application/msword, */*");
//			httpUrlConnection.setRequestProperty("Accept-Language", "zh-CN");
//			httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
//			httpUrlConnection
//					.setRequestProperty(
//							"User-Agent",
//							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
//									+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
//									+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

			httpUrlConnection.connect();// 连接

//			logger.debug("网址/ip位置:" + Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();

			if (HttpURLConnection.HTTP_OK == status) {
				is = httpUrlConnection.getInputStream();
				logger.info("下载图片:" + imageSource);
				is.close();
			} else {
				switch (status) {
					case HttpURLConnection.HTTP_FORBIDDEN:// 403
						logger.error("[403]:连接网址["+imageSource+"]禁止!");
						break;
					case HttpURLConnection.HTTP_NOT_FOUND:// 404
						logger.error("[404]:连接网址["+imageSource+"]不存在!");
						break;
					case HttpURLConnection.HTTP_INTERNAL_ERROR:// 500
						logger.error("[500]:连接网址["+imageSource+"]错误或不存在!");
						break;
					case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:// 504
						logger.error("[504]:连接网址["+imageSource+"]超时!");
						break;
				}
			}

		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->getInputStream]-网址["+imageSource+"]格式错误: " + mue.getMessage());
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->getInputStream]-网络连接有问题: " + e.getMessage());
		}
		return is;
	}
	// /**
	// * 获取图片的输入流
	// * @param imageSource 图片url
	// * @return
	// */
	// public static InputStream getInputStream(String imageSource) {
	// URL url;
	// InputStream is = null;
	// try {
	// url = new URL(imageSource);
	// // 打开连接
	// URLConnection con = url.openConnection();
	// // 输入流
	// is = con.getInputStream();
	//
	// is.close();
	// } catch (MalformedURLException e) {
	// // e.printStackTrace();
	// logger.error("MalformedURLException[ImageUtil->getInputStream]: " +
	// e.getMessage());
	// } catch (IOException e) {
	// // e.printStackTrace();
	// logger.error("IOException[ImageUtil->getInputStream]: " +
	// e.getMessage());
	// }
	//
	// return is;
	// }
}
