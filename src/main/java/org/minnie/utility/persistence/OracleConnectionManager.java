package org.minnie.utility.persistence;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.minnie.utility.util.Constant;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * c3p0数据库连接池(Oracle版)
 * @author wuf
 *
 */
public class OracleConnectionManager {
	
	private static Logger logger = Logger.getLogger(OracleConnectionManager.class.getName());
	
	private static ComboPooledDataSource ds;
    private static ResourceBundle rb;  
	private static BufferedInputStream inputStream;

	private OracleConnectionManager() {

	}

	static {
		String confFilePath = System.getProperty("user.dir")
				+ Constant.DB_POOL_CONFIGURE_C3P0_ORACLE;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					confFilePath));
			rb = new PropertyResourceBundle(inputStream);
			
			ds = new ComboPooledDataSource();
			ds.setDriverClass(rb.getString("jdbc.driverClass"));
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage());
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		} catch (PropertyVetoException pve) {
			logger.error(pve.getMessage());
		}
			
			//jdbc.url
			ds.setJdbcUrl(rb.getString("jdbc.url"));
			//jdbc.username
			ds.setUser(rb.getString("jdbc.username"));
			//jdbc.password
			ds.setPassword(rb.getString("jdbc.password"));
			//始化时创建的连接数，应在minPoolSize与maxPoolSize之间取值。默认为3
			ds.setInitialPoolSize(Integer.valueOf(rb.getString("initialPoolSize")).intValue());
			// 接池中保留的最大连接数。默认为15
			ds.setMaxPoolSize(Integer.valueOf(rb.getString("maxPoolSize")).intValue());
			//接池中保留的最小连接数
			ds.setMinPoolSize(Integer.valueOf(rb.getString("minPoolSize")).intValue());
			//当连接池中的连接用完时，C3P0一次性创建新连接的数目 默认 3
			ds.setAcquireIncrement(Integer.valueOf(rb.getString("acquireIncrement")).intValue());
			//定义在从数据库获取新连接失败后重复尝试获取的次数，默认为30
			ds.setAcquireRetryAttempts(Integer.valueOf(rb.getString("acquireRetryAttempts")).intValue());
			//两次连接中间隔时间，单位毫秒，默认为1000
			ds.setAcquireRetryDelay(Integer.valueOf(rb.getString("acquireRetryDelay")).intValue());
			//连接关闭时默认将所有未提交的操作回滚。默认为false
			ds.setAutoCommitOnClose(Boolean.valueOf(rb.getString("acquireRetryDelay")).booleanValue());
			/**
			 * 获取连接失败将会引起所有等待获取连接的线程抛出异常。但是数据源仍有效保留，并在下	
			 * 次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试获取连接
			 * 失败后该数据源将申明已断开并永久关闭。默认为false
			 */
			ds.setBreakAfterAcquireFailure(Boolean.valueOf(rb.getString("breakAfterAcquireFailure")).booleanValue());
			/**
			 * 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出
			 * SQLException，如设为0则无限期等待。单位毫秒，默认为0	
			 */
			ds.setCheckoutTimeout(Integer.valueOf(rb.getString("checkoutTimeout")).intValue());
			//最大空闲时间，超过空闲时间的连接将被丢弃。为0或负数则永不丢弃。默认为0
			ds.setMaxIdleTime(Integer.valueOf(rb.getString("maxIdleTime")).intValue());
			//每60秒检查所有连接池中的空闲连接。Default: 0
			ds.setIdleConnectionTestPeriod(Integer.valueOf(rb.getString("idleConnectionTestPeriod")).intValue());
			/**
			 * C3P0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能，	
			 * 通过多线程实现多个操作同时被执行。默认为3
			 */
			ds.setNumHelperThreads(Integer.valueOf(rb.getString("numHelperThreads")).intValue());
			//用户修改系统配置参数执行前最多等待的秒数。默认为300
			ds.setPropertyCycle(Integer.valueOf(rb.getString("propertyCycle")).intValue());

//			Properties props = new Properties();
//			props.put("remarksReporting", "true");
//			ds.setProperties(props);
//			logger.info(ds.toString());
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	/**
	 * 关闭ResultSet
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs){
		
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
	}
	
	/**
	 * 关闭Statement
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt){
		
		try {
			if(stmt != null){
				stmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 关闭CallableStatement
	 * @param cstmt
	 */
	public static void closeCallableStatement(CallableStatement cstmt){
		
		try {
			if(cstmt != null){
				cstmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭PreparedStatement
	 * @param pstmt
	 */
	public static void closePreparedStatement(PreparedStatement pstmt){
		
		try {
			if(pstmt != null){
				pstmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 关闭Connection
	 * @param conn
	 */
	public static void closeConnection(Connection conn){
		
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	
}
