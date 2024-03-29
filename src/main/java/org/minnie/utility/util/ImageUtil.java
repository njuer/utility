package org.minnie.utility.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.minnie.utility.module.hoopchina.HoopChina;
import org.minnie.utility.module.netease.Netease;
import org.minnie.utility.module.xinyingba.Video;
import org.minnie.utility.module.yangshengsuo.Regimen;

public class ImageUtil {

	private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);
	public static final int IO_BUFFER_SIZE = 8 * 1024;

	public static boolean save2File(String directory, Video video) {

		URL url;
		// InputStream is = null;
		BufferedOutputStream bos = null;

		String imageSource = video.getImageSource();
		Matcher matcher = urlPattern.matcher(imageSource);
		if (matcher.find()) {
			imageSource = matcher.group();
		} else if (Constant.IMG_NO_PIC.equals(imageSource)) {
			imageSource = Constant.URL_XINYINGBA + imageSource;
		} else {
			return false;
		}

		try {
			url = new URL(imageSource);

			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();

			// 设定timeout时间
			httpUrlConnection.setConnectTimeout(Constant.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(Constant.READ_TIMEOUT);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			// httpUrlConnection
			// .setRequestProperty(
			// "Accept",
			// "image/gif, image/jpeg, image/png, "
			// + "application/x-shockwave-flash, application/xaml+xml, "
			// + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
			// + "application/x-ms-application, application/vnd.ms-excel, "
			// + "application/vnd.ms-powerpoint, application/msword, */*");
			// httpUrlConnection.setRequestProperty("Accept-Language",
			// "zh-CN");
			// httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
			httpUrlConnection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
									+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
									+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

			httpUrlConnection.connect();// 连接

			// logger.debug("网址/ip位置:" +
			// Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();
			int length = httpUrlConnection.getContentLength();

			if (HttpURLConnection.HTTP_OK == status) {

				File dir = new File(directory);
				// 如果目录不存在
				if (!dir.exists()) {
					// 递归创建目录
					dir.mkdirs();
				}

				String path = directory
						+ File.separator
						+ imageSource
								.substring(imageSource.lastIndexOf("/") + 1);
				path = path.replaceAll("\\*", "");
				
				// 图片命名方式
				final File cacheFile = new File(path);
				
				if(cacheFile.exists()){
					logger.info(path + "已存在！");
					return true;
				}

				final InputStream is = new BufferedInputStream(
						httpUrlConnection.getInputStream(), IO_BUFFER_SIZE);

				bos = new BufferedOutputStream(new FileOutputStream(cacheFile),
						IO_BUFFER_SIZE);
				int read;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}

				logger.info("下载图片:" + imageSource);

				if (null != bos) {
					bos.close();
				}

				if (null != is) {
					is.close();
				}

				if (null != httpUrlConnection) {
					httpUrlConnection.disconnect();
				}

			} else {
				logger.error("Status code [" + status + "]: 图片[" + imageSource
						+ "]下载失败！");
				return false;
			}

		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->getPreparedStatementOfBinaryStream]-网址["
					+ imageSource + "]格式错误: " + mue.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->getPreparedStatementOfBinaryStream]-网络连接有问题: "
					+ e.getMessage());
			return false;
		}

		return true;

	}

	public static boolean save2File(String directory, String folder,
			String imageSource) {

		URL url;
		// InputStream is = null;
		BufferedOutputStream bos = null;

		// Matcher matcher = urlPattern.matcher(imageSource);
		// if (matcher.find()) {
		// imageSource = matcher.group();
		// } else {
		// return false;
		// }

		try {
			url = new URL(imageSource);

			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();

			// 设定timeout时间
			httpUrlConnection.setConnectTimeout(Constant.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(Constant.READ_TIMEOUT);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			// httpUrlConnection
			// .setRequestProperty(
			// "Accept",
			// "image/gif, image/jpeg, image/png, "
			// + "application/x-shockwave-flash, application/xaml+xml, "
			// + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
			// + "application/x-ms-application, application/vnd.ms-excel, "
			// + "application/vnd.ms-powerpoint, application/msword, */*");
			// httpUrlConnection.setRequestProperty("Accept-Language",
			// "zh-CN");
			// httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
			httpUrlConnection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
									+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
									+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

			httpUrlConnection.connect();// 连接

			// logger.debug("网址/ip位置:" +
			// Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();
			int length = httpUrlConnection.getContentLength();

			if (HttpURLConnection.HTTP_OK == status) {
				directory += File.separator + folder;
				File dir = new File(directory);
				// 如果目录不存在
				if (!dir.exists()) {
					// 递归创建目录
					dir.mkdirs();
				}

				String path = directory
						+ File.separator
						+ imageSource
								.substring(imageSource.lastIndexOf("/") + 1);
				path = path.replaceAll("\\*", "");
				
				// 图片命名方式
				final File cacheFile = new File(path);
				
				if(cacheFile.exists()){
					logger.info(path + "已存在！");
					return true;
				}

				final InputStream is = new BufferedInputStream(
						httpUrlConnection.getInputStream(), IO_BUFFER_SIZE);

				bos = new BufferedOutputStream(new FileOutputStream(cacheFile),
						IO_BUFFER_SIZE);
				int read;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}

				logger.info("下载图片:" + imageSource);

				if (null != bos) {
					bos.close();
				}

				if (null != is) {
					is.close();
				}

				if (null != httpUrlConnection) {
					httpUrlConnection.disconnect();
				}

			} else {
				logger.error("Status code [" + status + "]: 图片[" + imageSource
						+ "]下载失败！");
				return false;
			}

		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网址["
					+ imageSource + "]格式错误: " + mue.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网络连接有问题: "
					+ e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean save2File(String directory, HoopChina hoopChina) {
		
		URL url;
		BufferedOutputStream bos = null;
		String imageSource = hoopChina.getPirctureUrl();
		
		try {
			url = new URL(imageSource);
			
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			
			// 设定timeout时间
			httpUrlConnection.setConnectTimeout(Constant.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(Constant.READ_TIMEOUT);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			// httpUrlConnection
			// .setRequestProperty(
			// "Accept",
			// "image/gif, image/jpeg, image/png, "
			// + "application/x-shockwave-flash, application/xaml+xml, "
			// + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
			// + "application/x-ms-application, application/vnd.ms-excel, "
			// + "application/vnd.ms-powerpoint, application/msword, */*");
			// httpUrlConnection.setRequestProperty("Accept-Language",
			// "zh-CN");
			// httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
			httpUrlConnection
			.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
							+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
							+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			
			httpUrlConnection.connect();// 连接
			
			// logger.debug("网址/ip位置:" +
			// Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();
//			int length = httpUrlConnection.getContentLength();
			
			if (HttpURLConnection.HTTP_OK == status) {
				directory += File.separator + hoopChina.getTitle()+ "_" + hoopChina.getTotal();
				File dir = new File(directory);
				// 如果目录不存在
				if (!dir.exists()) {
					// 递归创建目录
					dir.mkdirs();
				}
				
				String path = directory
						+ File.separator
						+ imageSource
						.substring(imageSource.lastIndexOf("/") + 1);
				path = path.replaceAll("\\*", "");
				
				// 图片命名方式
				final File cacheFile = new File(path);
				
				if(cacheFile.exists()){
					logger.info(path + "已存在！");
					return true;
				}
				
				final InputStream is = new BufferedInputStream(
						httpUrlConnection.getInputStream(), IO_BUFFER_SIZE);
				
				bos = new BufferedOutputStream(new FileOutputStream(cacheFile),
						IO_BUFFER_SIZE);
				int read;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}
				
//				logger.info("下载图片:" + imageSource);
				
				if (null != bos) {
					bos.close();
				}
				
				if (null != is) {
					is.close();
				}
				
				if (null != httpUrlConnection) {
					httpUrlConnection.disconnect();
				}
				
			} else {
				logger.error("Status code [" + status + "]: 图片[" + imageSource
						+ "]下载失败！");
				return false;
			}
			
		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网址["
					+ imageSource + "]格式错误: " + mue.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网络连接有问题: "
					+ e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean save2File(String directory, Netease netease) {
		
		URL url;
		BufferedOutputStream bos = null;
		String imageSource = netease.getImg();
		
		try {
			url = new URL(imageSource);
			
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			
			// 设定timeout时间
			httpUrlConnection.setConnectTimeout(Constant.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(Constant.READ_TIMEOUT);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			// httpUrlConnection
			// .setRequestProperty(
			// "Accept",
			// "image/gif, image/jpeg, image/png, "
			// + "application/x-shockwave-flash, application/xaml+xml, "
			// + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
			// + "application/x-ms-application, application/vnd.ms-excel, "
			// + "application/vnd.ms-powerpoint, application/msword, */*");
			// httpUrlConnection.setRequestProperty("Accept-Language",
			// "zh-CN");
			// httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
			httpUrlConnection
			.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
							+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
							+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			
			httpUrlConnection.connect();// 连接
			
			// logger.debug("网址/ip位置:" +
			// Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();
//			int length = httpUrlConnection.getContentLength();
			
			if (HttpURLConnection.HTTP_OK == status) {
				StringBuffer sb = new StringBuffer();
				sb.append(directory);
				sb.append(File.separator);
				sb.append(netease.getSubject());
				sb.append("_");
				sb.append(netease.getSize());
				
				
//				directory += File.separator + netease.getSubject()+ "_" + netease.getSize();
				File dir = new File(sb.toString());
				// 如果目录不存在
				if (!dir.exists()) {
					// 递归创建目录
					dir.mkdirs();
				}
				
//				String path = directory + File.separator;
				sb.append(File.separator);
				if(StringUtils.isBlank(netease.getTitle())){
//					path += netease.getId();
					sb.append(netease.getId());
				} else {
//					path += netease.getTitle();
					sb.append(netease.getTitle());
				}
//				String path = directory
//						+ File.separator
//						+ imageSource
//						.substring(imageSource.lastIndexOf("/") + 1);
				sb.append(imageSource.substring(imageSource.lastIndexOf(".")));
				String path = sb.toString();
				System.out.println(path);
				path = path.replaceAll("\\*", "");
				
				// 图片命名方式
				final File cacheFile = new File(path);
				
				if(cacheFile.exists()){
					logger.info(path + "已存在！");
					return true;
				}
				
				final InputStream is = new BufferedInputStream(
						httpUrlConnection.getInputStream(), IO_BUFFER_SIZE);
				
				bos = new BufferedOutputStream(new FileOutputStream(cacheFile),
						IO_BUFFER_SIZE);
				int read;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}
				
//				logger.info("下载图片:" + imageSource);
				
				if (null != bos) {
					bos.close();
				}
				
				if (null != is) {
					is.close();
				}
				
				if (null != httpUrlConnection) {
					httpUrlConnection.disconnect();
				}
				
			} else {
				logger.error("Status code [" + status + "]: 图片[" + imageSource
						+ "]下载失败！");
				return false;
			}
			
		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网址["
					+ imageSource + "]格式错误: " + mue.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网络连接有问题: "
					+ e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean save2File(String directory, Regimen regimen) {
		
		URL url;
		BufferedOutputStream bos = null;
		String imageSource = regimen.getPictureUrl();
		
		if(null == imageSource || StringUtil.isBlank(imageSource)){
			return false;
		}
		
		try {
			url = new URL(imageSource);
			
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			
			// 设定timeout时间
			httpUrlConnection.setConnectTimeout(Constant.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(Constant.READ_TIMEOUT);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			// httpUrlConnection
			// .setRequestProperty(
			// "Accept",
			// "image/gif, image/jpeg, image/png, "
			// + "application/x-shockwave-flash, application/xaml+xml, "
			// + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
			// + "application/x-ms-application, application/vnd.ms-excel, "
			// + "application/vnd.ms-powerpoint, application/msword, */*");
			// httpUrlConnection.setRequestProperty("Accept-Language",
			// "zh-CN");
			// httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 设置浏览器类型和版本、操作系统，使用语言等信息
			httpUrlConnection
			.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; "
							+ ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; "
							+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			
			httpUrlConnection.connect();// 连接
			
			// logger.debug("网址/ip位置:" +
			// Inet4Address.getByName(url.getHost()));
			int status = httpUrlConnection.getResponseCode();
//			int length = httpUrlConnection.getContentLength();
			
			if (HttpURLConnection.HTTP_OK == status) {
				StringBuffer sb = new StringBuffer();
				sb.append(directory);
				sb.append(File.separator);
				sb.append(regimen.getTitle());
				sb.append("_");
				sb.append(regimen.getTotal());
				
				File dir = new File(sb.toString());
				// 如果目录不存在
				if (!dir.exists()) {
					// 递归创建目录
					dir.mkdirs();
				}
				
				sb.append(File.separator);
				sb.append(imageSource.substring(imageSource.lastIndexOf("/") + 1));
				
				String path = sb.toString();
				path = path.replaceAll("\\*", "");
				
				// 图片命名方式
				final File cacheFile = new File(path);
				
				if(cacheFile.exists()){
					logger.info(path + "已存在！");
					return true;
				}
				
				final InputStream is = new BufferedInputStream(
						httpUrlConnection.getInputStream(), IO_BUFFER_SIZE);
				
				bos = new BufferedOutputStream(new FileOutputStream(cacheFile),
						IO_BUFFER_SIZE);
				int read;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}
				
//				logger.info("下载图片:" + imageSource);
				
				if (null != bos) {
					bos.close();
				}
				
				if (null != is) {
					is.close();
				}
				
				if (null != httpUrlConnection) {
					httpUrlConnection.disconnect();
				}
				
			} else {
				logger.error("Status code [" + status + "]: 图片[" + imageSource
						+ "]下载失败！");
				return false;
			}
			
		} catch (MalformedURLException mue) {
			logger.error("MalformedURLException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网址["
					+ imageSource + "]格式错误: " + mue.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException[ImageUtil->save2File(String directory, String folder, String imageSource)]-网络连接有问题: "
					+ e.getMessage());
			return false;
		}
		return true;
	}
}