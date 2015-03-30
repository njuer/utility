package org.minnie.utility.persistence;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.autocode.NamingRuleConvert;
import org.minnie.autocode.TableMetaInfo;
import org.minnie.utility.util.Constant;

public class ConnectionManager {

	private static Logger logger = Logger.getLogger(ConnectionManager.class
			.getName());

	public static void main(String[] args) {
		//example,表名：user
		
		ConnectionManager cm = new ConnectionManager();
		//Oracle
		Connection conn = cm.getConnection(Constant.DB_POOL_CONFIGURE_C3P0_ORACLE);
		if(null != conn){
			cm.getColumnInfo(conn, "user");
		}

		//MySQL
//		Connection conn = cm.getConnection(Constant.DB_POOL_CONFIGURE_C3P0_MYSQL);
//		if(null != conn){
//			cm.getColumnInfo(conn, "user");
//		}
		
		//SQL Server
//		Connection conn = cm.getConnection(Constant.DB_POOL_CONFIGURE_C3P0_SQL_SERVER);
//		if(null != conn){
//			cm.getColumnInfo(conn, "[user]");//注意，表名外要加方括号
//		}
	}
	
	
	public Connection getConnection(){
		return getConnection(Constant.DB_POOL_CONFIGURE_C3P0_ORACLE);
	}
	
	/**
	 * 获取数据库链接
	 * @param conf	c3p0配置文件
	 * @return
	 */
	public Connection getConnection(String conf) {
		
	    ResourceBundle rb;  
		BufferedInputStream inputStream;
		
		String confFilePath = System.getProperty("user.dir") + conf;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					confFilePath));
			rb = new PropertyResourceBundle(inputStream);
		
			String userName = rb.getString("jdbc.username");//jdbc.username
			String password = rb.getString("jdbc.password");//jdbc.password
			String driverClazz = rb.getString("jdbc.driverClass");//jdbc.driverClass
			String url = rb.getString("jdbc.url");//jdbc.url

			return getConnection(url, driverClazz, userName, password, true);
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage());
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}
		return null;
	}
	
	public Connection getConnection(String url, String driverClazz, String userName, String password, boolean remarksReporting) {
		
		Connection connection = null;
		
		Properties props = new Properties();
		props.put("user", userName);//jdbc.username
		props.put("password", password);//jdbc.password
		props.put("remarksReporting", String.valueOf(remarksReporting));//要获取Oracle字段注释，必须将remarksReporting设置为true
		
		try {
			Class.forName(driverClazz);
			connection = DriverManager.getConnection(url, props);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public Connection getConnection(String url, String driverClazz, String userName, String password) {
		return getConnection(url, driverClazz, userName, password, true);
	}

	public List<TableMetaInfo> getColumnInfo(String tableName) {
		return getColumnInfo(getConnection(), tableName);
	}
	/**
	 * 获取某表的字段信息
	 * @param connection	数据库连接
	 * @param tableName	表名
	 * @return
	 */
	public List<TableMetaInfo> getColumnInfo(Connection connection, String tableName) {

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		DatabaseMetaData dmd = null;
		List<TableMetaInfo> list = new ArrayList<TableMetaInfo>();

		Map<String, String> remarkMap = new HashMap<String, String>();
		Set<String> ignoreColumnSet = new HashSet<String>();
		 ignoreColumnSet.add("id");//主键
		 ignoreColumnSet.add("name");//名称
		 ignoreColumnSet.add("remarks");//备注
		 ignoreColumnSet.add("createBy");//创建者
		 ignoreColumnSet.add("createDate");//创建日期
		 ignoreColumnSet.add("updateBy");//更新者
		 ignoreColumnSet.add("updateDate");//更新日期
		 ignoreColumnSet.add("delFlag");//删除标记（0：正常；1：删除；2：审核）

		/**
		 * 获取connection元数据信息
		 */
		try {
			conn = connection;
			pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData(); // 获取字段名
			dmd = conn.getMetaData();

			ResultSet rsColumns = dmd.getColumns(null, "%", tableName, "%");

			while (rsColumns.next()) {
				remarkMap.put(rsColumns.getString("COLUMN_NAME"),
						rsColumns.getString("REMARKS"));
			}

			if (rsmd != null) {
				int count = rsmd.getColumnCount();
				for (int i = 1; i <= count; i++) {
					TableMetaInfo metaInfo = new TableMetaInfo();
					
					String columnName = rsmd.getColumnName(i);//字段名称
					metaInfo.setColumnName(columnName);
					
					String propertyName = NamingRuleConvert.field2Variable(columnName, "_", "");
					
					if (ignoreColumnSet.contains(propertyName)) {
						continue;
					}
					
					metaInfo.setPropertyName(propertyName);//对应Java属性名称
					
					String columnClassName = rsmd.getColumnClassName(i);//对应Java完整类名
					metaInfo.setFullyQualifiedName(columnClassName);
					
					int colDisplaySize = rsmd.getColumnDisplaySize(i);
					metaInfo.setColumnDisplaySize(colDisplaySize);//字段长度
					
					String clazzName = columnClassName
							.substring(columnClassName.lastIndexOf(".") + 1);
					if ("Timestamp".equals(clazzName) || "Date".equals(clazzName)) {
						clazzName = "String";
					} else if("Long".equals(clazzName)) {
						if(colDisplaySize <= 11){
							clazzName = "Integer";
						}
					}
					metaInfo.setClazzName(clazzName);//对应Java类名

					String remark = remarkMap.get(columnName);
					metaInfo.setRemark(null == remark?StringUtils.EMPTY:remark);
					logger.info(metaInfo.toString());
					 list.add(metaInfo);
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			closeStatement(stmt);
			closePreparedStatement(pstmt);
			closeConnection(conn);
		}
		return list;
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param rs
	 */
	public void closeResultSet(ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭Statement
	 * 
	 * @param stmt
	 */
	public void closeStatement(Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭CallableStatement
	 * 
	 * @param cstmt
	 */
	public void closeCallableStatement(CallableStatement cstmt) {

		try {
			if (cstmt != null) {
				cstmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭PreparedStatement
	 * 
	 * @param pstmt
	 */
	public void closePreparedStatement(PreparedStatement pstmt) {

		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭Connection
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

}
