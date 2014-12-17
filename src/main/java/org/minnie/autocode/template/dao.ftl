package ${packageName}.${moduleName}.dao${subModuleName};

import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.utility.persistence.MysqlConnectionManager;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.DateUtil;

/**
 * ${functionName}DAO
 * @author ${classAuthor}
 * @version ${classVersion}
 */
public class ${ClassName}Dao {

	private static Logger logger = Logger.getLogger(${ClassName}Dao.class.getName());

	/**
	 * 批量导入${functionName}
	 * 
	 * @param ${className}List
	 *            ${functionName}列表
	 * @param existIdSet
	 */
	public static void batchAdd${ClassName}(List<${ClassName}> ${className}List, Set<Long> existIdSet) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		boolean existFlag = true;
		if (null == existIdSet || 0 == existIdSet.size()) {
			existFlag = false;
		}

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" ${tableName} ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");	// id
		sqlInsert.append("name,");// 名称
	    <#list propertyList as pl>
	    sqlInsert.append("${pl.columnName},");	// ${pl.comment}
	    </#list>
		sqlInsert.append("del_flag,");		// 删除标记（0：正常；1：删除；2：审核）
		sqlInsert.append("create_date,");	// 创建日期
		sqlInsert.append("create_by,");		// 创建者
		sqlInsert.append("update_date,");	// 更新日期
		sqlInsert.append("update_by,");		// 更新者
		sqlInsert.append("remarks");		// 备注
		sqlInsert.append(" ) ");
		sqlInsert.append(" values ");
		sqlInsert.append(" ( ");
		sqlInsert.append(" ?, ");		// id
		sqlInsert.append(" ?, ");		// 名称
	    <#list propertyList as pl>
	   	sqlInsert.append(" ?, ");		// ${pl.comment}
	    </#list>
		sqlInsert.append(" ?, ");		// 删除标记（0：正常；1：删除；2：审核）
		sqlInsert.append(" ?, ");		// 创建日期
		sqlInsert.append(" ?, ");		// 创建者
		sqlInsert.append(" ?, ");		// 更新日期
		sqlInsert.append(" ?, ");		// 更新者
		sqlInsert.append(" ? ");		// 备注
		sqlInsert.append(" ) ");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" ${tableName} ");
		sqlUpdate.append("set ");
		sqlUpdate.append(" name = ?,");	// 更新日期
	    <#list propertyList as pl>
	    sqlUpdate.append("${pl.columnName} = ?,");	// ${pl.comment}
	    </#list>
		sqlUpdate.append(" update_date = ?,");	// 更新日期
		sqlUpdate.append(" update_by = ? ");		// 更新者
		sqlUpdate.append("where id = ? ");		//id

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate.toString());

				for (${ClassName} ${className} : ${className}List) {
					Long id = ${className}.getId();

					if (existFlag && existIdSet.contains(id)) {
						// 更新
						pstUpdate.setString(1, ${className}.getName());
						<#assign x=1 />
					    <#list propertyList as pl>
					    pstUpdate.set${pl.javaType}(${pl_index + 2}, ${className}.get${pl.methodName}());// ${pl.comment}
					    <#assign x=x+1 />
					    </#list>
						<#assign x=x+1 />
						pstUpdate.setString(${x}, DateUtil.getTime(System.currentTimeMillis()));
						<#assign x=x+1 />
						pstUpdate.setString(${x}, "admin");
						<#assign x=x+1 />
						pstUpdate.setLong(${x}, ${className}.getId());
						// 把一个SQL命令加入命令列表
						pstUpdate.addBatch();
					} else {
						// 新增
						pstInsert.setLong(1, ${className}.getId());
						pstInsert.setString(2, ${className}.getName());
						<#assign y=2 />
					    <#list propertyList as pl>
					    pstInsert.set${pl.javaType}(${pl_index + 3}, ${className}.get${pl.methodName}());// ${pl.comment}
					    <#assign y=y+1 />
					    </#list>
					    <#assign y=y+1 />
						pstInsert.setString(${y}, "0");// 删除标记（0：正常；1：删除；2：审核）
						String dt = DateUtil.getTime(System.currentTimeMillis());
						<#assign y=y+1 />
						pstInsert.setString(${y}, dt);// 创建日期
						<#assign y=y+1 />
						pstInsert.setString(${y}, "1");// 创建者
						<#assign y=y+1 />
						pstInsert.setString(${y}, dt);// 更新日期
						<#assign y=y+1 />
						pstInsert.setString(${y}, "1");// 更新者
						<#assign y=y+1 />
						pstInsert.setNull(${y}, Types.VARCHAR);// 备注
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入${functionName}成功！");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closePreparedStatement(pstUpdate);
			MysqlConnectionManager.closeConnection(conn);
		}
	}
	
	/**
	 * 获取${functionName}列表
	 * 
	 * @param sql
	 * @return
	 */
	public static List<${ClassName}> get${ClassName}List(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<${ClassName}> list = new ArrayList<${ClassName}>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT ");
					sb.append("id,");	// id
					sb.append("name,");// 名称
				    <#list propertyList as pl>
				    sb.append("${pl.columnName},");	// ${pl.comment}
				    </#list>
					sb.append("del_flag,");		// 删除标记（0：正常；1：删除；2：审核）
					sb.append("create_date,");	// 创建日期
					sb.append("create_by,");		// 创建者
					sb.append("update_date,");	// 更新日期
					sb.append("update_by,");		// 更新者
					sb.append("remarks");		// 备注
					sb.append(" FROM ");
					sb.append(" ${tableName} ");		// 表名
					sql = sb.toString();
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					${ClassName} ${className} = new ${ClassName}();
					${className}.setId(rs.getLong(1));
					${className}.setName(rs.getString(2));
				    <#list propertyList as pl>
				    ${className}.set${pl.methodName}(rs.get${pl.javaType}(${pl_index + 3}));
				    </#list>

					list.add(${className});
				}
				logger.info("已获取${functionName}");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}


}