package org.minnie.autocode;

import java.sql.Connection;

import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.persistence.ConnectionManager;
import org.minnie.utility.util.Constant;

public class Test {

	public static void main(String[] args) {
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir") + Constant.LOG_LOG4J_PARAM_FILE);

		ConnectionManager cm = new ConnectionManager();
//		Connection conn = cm.getConnection();
		cm.getColumnInfo("[Index]");
	}

}
