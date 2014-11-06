package org.minnie.utility.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SystemServiceUtil {
	
	private static Logger logger = Logger.getLogger(SystemServiceUtil.class.getName());

	private static Runtime runtime = Runtime.getRuntime();
	
	/**
	 * 通过批处理文件启动MySQL
	 * @return
	 */
	public static boolean startMysql(){
		return startMysql(null);
	}
	
	/**
	 * 通过批处理文件启动MySQL
	 * @param batPath	批处理文件路径
	 * @return
	 */
	public static boolean startMysql(String batPath){
		
		if(StringUtils.isBlank(batPath)){
			batPath = System.getProperty("user.dir") + Constant.MYSQL_START_BAT_PATH;
		}
		
		Process ps = null;
		try {
			ps = runtime.exec("cmd.exe /C start " + batPath);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean result = 0 ==ps.exitValue();
		if (result) {
			logger.info("Mysql启动完成！");
		} else {
			logger.info("Mysql启动失败！");
		}
		
		return result;
	}
	
	/**
	 * 通过批处理文件关闭MySQL
	 * @return
	 */
	public static boolean stopMysql(){
		return startMysql(null);
	}
	
	/**
	 * 通过批处理文件关闭MySQL
	 * @param batPath	批处理文件路径
	 * @return
	 */
	public static boolean stopMysql(String batPath){
		
		if(StringUtils.isBlank(batPath)){
			batPath = System.getProperty("user.dir") + Constant.MYSQL_STOP_BAT_PATH;
		}

		Process ps = null;
		try {
			ps = runtime.exec("cmd.exe /C start " + batPath);
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean result = 0 ==ps.exitValue();
		if (result) {
			logger.info("Mysql关闭完成！");
		} else {
			logger.info("Mysql关闭失败！");
		}
		
		return result;
	}
	
	/**
	 * 获取DOS命令输出结果流
	 * @param command	DOS命令
	 */
	public static InputStream getResponseStream(String command){
		InputStream in = null;
		try {
			Process process = Runtime.getRuntime().exec(command);
			in = process.getInputStream();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return in;
	}

	/**
	 * 获取DOS命令输出结果
	 * @param command
	 */
	public static String getResponse(String command){
		StringBuffer out = new StringBuffer();
		InputStream in = getResponseStream(command);
		byte[] b = new byte[4096];
		try {
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return out.toString();
	}
	
	public static void main(String[] args) {
		logger.info(getResponse("ping www.baidu.com"));
	}
	
}