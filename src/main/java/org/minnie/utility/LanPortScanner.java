package org.minnie.utility;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.util.Constant;

/**
 * 局域网端口扫描
 * @author eazytec
 *
 */
public class LanPortScanner {
	
	private static Logger logger = Logger.getLogger(LanPortScanner.class.getName());

    public LanPortScanner(String IP, int port) {

    }

    public static void main(String[] args) throws IOException, InterruptedException {

		// 获取加载系统配置时间
		long startTime = System.currentTimeMillis();
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);
    	
        String ipHead;
        int lastPoint;
        String IP = InetAddress.getLocalHost().getHostAddress();

        lastPoint = IP.lastIndexOf('.');

        ipHead = IP.substring(0, ++lastPoint);

        /**
         * ip从198开始，到254结束，端口8080
         */
        for (int tail = 198; tail < 255; tail++) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.invokeAll(Arrays.asList(new Task(ipHead, tail, 8080)),
                    200, TimeUnit.MILLISECONDS);
            executor.shutdown();
        }

		long endTime = System.currentTimeMillis();
		logger.info("程序运行耗时：" + (endTime - startTime) /1000 + "s"); 
    }
}

class Task implements Callable<String> {
    String ipHead;
    int ipTail;
    int port;
	private static Logger logger = Logger.getLogger(Task.class.getName());


    public Task(String ipHead, int ipTail, int port) {
        this.ipHead = ipHead;
        this.ipTail = ipTail;
        this.port = port;
    }

    public String call() throws Exception {
        Socket connect = new Socket();
        connect.setSoTimeout(100);
            try {
                connect = new Socket(this.ipHead + ipTail, this.port);
                connect.close();

                logger.info(this.ipHead + ipTail + ":" + this.port);

            } catch (UnknownHostException e) {
//                logger.error("Unknown Port：" + this.ipHead + ipTail + " " + this.port);
            } catch (IOException e) {
//            	logger.error("Unknown Port：" + this.ipHead + ipTail + " " + this.port);
            } 
        
//        logger.info("Finished!");
        return null;

    }
}