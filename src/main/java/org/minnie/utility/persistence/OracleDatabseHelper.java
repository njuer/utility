package org.minnie.utility.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.autocode.NamingRuleConvert;



public class OracleDatabseHelper {

	private static Logger logger = Logger.getLogger(OracleDatabseHelper.class
			.getName());
	

	public static List<String[]> getTableInfo(String jdbcTable) {
		
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		DatabaseMetaData dmd = null;
		
		List<String[]> list = new ArrayList<String[]>();
		Set<String> ignoreColumnSet = new HashSet<String>();
//		ignoreColumnSet.add("id");//主键
//		ignoreColumnSet.add("name");//名称
//		ignoreColumnSet.add("remarks");//备注
//		ignoreColumnSet.add("createBy");//创建者
//		ignoreColumnSet.add("createDate");//创建日期
//		ignoreColumnSet.add("updateBy");//更新者
//		ignoreColumnSet.add("updateDate");//更新日期
//		ignoreColumnSet.add("delFlag");//删除标记（0：正常；1：删除；2：审核）
		
		try {
			conn = OracleConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM " + jdbcTable);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData(); // 获取字段名
			dmd = conn.getMetaData();
	
			
			
//			// 获取字段注释
//			ResultSet rsColumns = dmd.getColumns(null, "%", jdbcTable, "%");
//			List<String> comments = new ArrayList<String>();
//			while (rsColumns.next()) {
//				comments.add(rsColumns.getString("REMARKS"));
//			}
			
			jdbcTable = jdbcTable.toUpperCase();
			List<String> comments = new ArrayList<String>();
			String category = conn.getCatalog();
			ResultSet rsColumns = dmd.getColumns(category, "JCY", jdbcTable, "%");
			while (rsColumns.next()) {
				comments.add(rsColumns.getString("REMARKS"));
				logger.info(rsColumns.getString("REMARKS"));
			}
			
			
//			jdbcTable = jdbcTable.toUpperCase();
//			ResultSet resultSet = dmd.getTables(conn.getCatalog(), "JCY", null, new String[] { "TABLE" });
//			List<String> comments = new ArrayList<String>();
//			while (resultSet.next()) {
//				String t = resultSet.getString("TABLE_NAME");
//				if (t.equals(jdbcTable)) {
//					ResultSet rs1 = dmd.getColumns(conn.getCatalog(), "JCY", jdbcTable, "%");
//					while (rs1.next()) {
//						comments.add(rs1.getString("REMARKS"));
//					}
//				}
//			}

			
			if (rsmd != null) {
				int count = rsmd.getColumnCount();
				for (int i = 1; i <= count; i++) {
					String javaType = rsmd.getColumnClassName(i);
					int columnDisplaySize = rsmd.getColumnDisplaySize(i);
					String columnName = StringUtils.lowerCase(rsmd.getColumnName(i));
					String propertyName = NamingRuleConvert.field2Variable(columnName, "_", "");
					if(ignoreColumnSet.contains(propertyName)){
						continue;
					}
//					String len = null;
					javaType = javaType.substring(javaType.lastIndexOf(".") + 1);
					if ("Timestamp".equals(javaType)) {
						javaType = "String";
					}
//					if ("String".equals(javaType)) {
//						len = String.valueOf(columnDisplaySize);
//					}
					String[] str = new String[] {javaType, propertyName, columnName, comments.get(i - 1), String.valueOf(columnDisplaySize)};
					list.add(str);
				}
			}
		} catch (SQLException ex2) {
			ex2.printStackTrace();
		} catch (Exception ex2) {
			ex2.printStackTrace();
		} finally {
			OracleConnectionManager.closeStatement(stmt);
			OracleConnectionManager.closePreparedStatement(pstmt);
			OracleConnectionManager.closeConnection(conn);
		}
		
		return list;
	}
	
}