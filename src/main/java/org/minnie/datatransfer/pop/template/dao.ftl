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
import org.minnie.utility.persistence.OracleConnectionManager;
import org.minnie.utility.persistence.OracleDatabseHelper;
import org.minnie.utility.persistence.DateUtil;

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
	public void batchAdd${ClassName}(List<${ClassName}> ${className}List) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" ${tableName} ");
		sqlInsert.append(" ( ");
	    <#list propertyList as pl>
	    sqlInsert.append("${pl.columnName},");
	    </#list>
		sqlInsert.append(" ) ");
		sqlInsert.append(" values ");
		sqlInsert.append(" ( ");
	    <#list propertyList as pl>
	   	sqlInsert.append(" ?, ");	
	    </#list>
		sqlInsert.append(" ) ");

		try {
			conn = OracleConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert.toString());

				for (${ClassName} ${className} : ${className}List) {
					<#assign y=1 />
				    <#list propertyList as pl>
				    pstInsert.set${pl.javaType}(${pl_index + 1}, ${className}.get${pl.methodName}());
				    <#assign y=y+1 />
				    </#list>
					// 把一个SQL命令加入命令列表
					pstInsert.addBatch();
				}

				// 执行批量更新
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入${functionName}成功！");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			OracleConnectionManager.closePreparedStatement(pstInsert);
			OracleConnectionManager.closePreparedStatement(pstUpdate);
			OracleConnectionManager.closeConnection(conn);
		}
	}
	
}