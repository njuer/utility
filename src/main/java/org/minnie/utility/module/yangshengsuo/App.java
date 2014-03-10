package org.minnie.utility.module.yangshengsuo;

import org.apache.log4j.Logger;
import org.minnie.utility.util.HtmlUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-8
 * 39养生所
 */
public class App {

	private static Logger logger = Logger.getLogger(App.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(HtmlUtil.getTotal39YangShengsuo("http://www.ttyl5.com/yingshi/new/3773_3.html"));
	}

}
