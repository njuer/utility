package org.minnie.autocode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
//import org.springframework.core.io.DefaultResourceLoader;
//
//import com.google.common.collect.Maps;
//import com.thinkgem.jeesite.common.utils.DateUtils;
//import com.
import freemarker.template.Template;

//import com.thinkgem.jeesite.common.config.Global;

/**
 * 代码生成器 改进版
 * @author neiplz
 * @version 2014-10-24
 */
public class CodeGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);
	// 获取工程路径
	private static String projectPath = System.getProperty("user.dir");

	public static void main(String[] args) throws Exception {

		// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================

		
		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(projectPath + Constant.LOG_LOG4J_PARAM_FILE);
		logger.info("Project Path: {}", projectPath);
		
		// 主要提供基本功能模块代码生成。
		// 目录生成结构：{packageName}/{moduleName}/{dao,entity,service,web}/{subModuleName}/{className}
		
		// packageName 包名，这里如果更改包名，请在applicationContext.xml和srping-mvc.xml中配置base-package、packagesToScan属性，来指定多个（共4处需要修改）。
		String packageName = "org.minnie.utility.module";
		
		String moduleName = "netease";			// 模块名，例：sys
		String subModuleName = "";				// 子模块名（可选） 
		String className = "FootballTeamTwo";			// 类名，例：user
		String classAuthor = "neiplz";		// 类作者，例：ThinkGem
		String functionName = "球队信息";			// 功能名，例：用户
		String tableName = "lms_football_team";		//表名，例：sys_dict
		
		// 是否启用生成工具
		Boolean isEnable = true;			
		
		// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================
		
		if (!isEnable){
			logger.error("请启用代码生成工具，设置参数：isEnable = true");
			return;
		}
		
		if (StringUtils.isBlank(moduleName) || StringUtils.isBlank(moduleName) 
				|| StringUtils.isBlank(className) || StringUtils.isBlank(functionName)){
			logger.error("参数设置错误：包名、模块名、类名、功能名不能为空。");
			return;
		}
		
		Set<String> moduleSet = new HashSet<String>();
		moduleSet.add("Entity");
		moduleSet.add("Dao");
		
		// 获取文件分隔符
		String separator = File.separator;
		
		
//		File projectPath = new DefaultResourceLoader().getResource("").getFile();
//		while(!new File(projectPath.getPath()+separator+"src"+separator+"main").exists()){
//			projectPath = projectPath.getParentFile();
//		}
		
		// 模板文件路径
		String tplPath = StringUtils.replace(projectPath+"/src/main/java/org/minnie/autocode/template", "/", separator);
		logger.info("Template Path: {}", tplPath);
		
		// Java文件路径
		String javaPath = StringUtils.replaceEach(projectPath+"/src/main/java/"+StringUtils.lowerCase(packageName), 
				new String[]{"/", "."}, new String[]{separator, separator});
		logger.info("Java Path: {}", javaPath);
		
		// 代码模板配置
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
		cfg.setDirectoryForTemplateLoading(new File(tplPath));

		// 定义模板变量
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("packageName", StringUtils.lowerCase(packageName));
		model.put("moduleName", StringUtils.lowerCase(moduleName));
		model.put("subModuleName", StringUtils.isNotBlank(subModuleName)?"."+StringUtils.lowerCase(subModuleName):"");
		model.put("className", StringUtils.uncapitalize(className));
		model.put("ClassName", StringUtils.capitalize(className));
		model.put("classAuthor", StringUtils.isNotBlank(classAuthor)?classAuthor:"Generate Tools");
		model.put("classVersion", DateUtil.getDate());
		model.put("functionName", functionName);
		model.put("tableName", tableName);
		model.put("urlPrefix", model.get("moduleName")+(StringUtils.isNotBlank(subModuleName)
				?"/"+StringUtils.lowerCase(subModuleName):"")+"/"+model.get("className"));
//		model.put("viewPrefix", //StringUtils.substringAfterLast(model.get("packageName"),".")+"/"+
//				model.get("urlPrefix"));
//		model.put("permissionPrefix", model.get("moduleName")+(StringUtils.isNotBlank(subModuleName)
//				?":"+StringUtils.lowerCase(subModuleName):"")+":"+model.get("className"));
//
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		List<String[]> orm = MysqlDatabseHelper.getTableInfo(tableName);
		for (int i = 0 ; i < orm.size() ; i++) {
			Map<String, Object> tmp = new HashMap<String, Object>();
			String jType = orm.get(i)[0];
			String variableName = orm.get(i)[1];
			String columnName = orm.get(i)[2];
			String comment = orm.get(i)[3];
			String len = orm.get(i)[4];
//			tmp.put("index", i);
			tmp.put("propertyName", variableName);
			tmp.put("javaType", jType);
			tmp.put("columnName", columnName);
			tmp.put("methodName", NamingRuleConvert.firstLetterToUpperCase(variableName));
			tmp.put("comment", comment);
			tmp.put("length", len);
			result.add(tmp);
		}
		model.put("propertyList", result);
		
		
		Template template = null;
		String content = null;
		String filePath = null;
		
		if(moduleSet.contains("Entity")){
			// 生成 Entity
			template = cfg.getTemplate("entity.ftl");
			content = renderTemplate(template, model);
			StringBuffer entityPathBuffer = new StringBuffer();
			entityPathBuffer.append(javaPath);
			entityPathBuffer.append(separator);
			entityPathBuffer.append(model.get("moduleName"));
			entityPathBuffer.append(separator);
			entityPathBuffer.append("entity");
			entityPathBuffer.append(separator);
			if(StringUtils.isNotBlank(subModuleName)){
				entityPathBuffer.append(StringUtils.lowerCase(subModuleName));
				entityPathBuffer.append(separator);
			}
			entityPathBuffer.append(model.get("ClassName"));
			entityPathBuffer.append(".java");
			filePath = entityPathBuffer.toString();
			
			writeFile(content, filePath);
			logger.info("Entity: {}", filePath);
		}

		if(moduleSet.contains("Dao")){
			// 生成 Dao
			template = cfg.getTemplate("dao.ftl");
			content = renderTemplate(template, model);
			StringBuffer daoPathBuffer = new StringBuffer();
			daoPathBuffer.append(javaPath);
			daoPathBuffer.append(separator);
			daoPathBuffer.append(model.get("moduleName"));
			daoPathBuffer.append(separator);
			daoPathBuffer.append("dao");
			daoPathBuffer.append(separator);
			if(StringUtils.isNotBlank(subModuleName)){
				daoPathBuffer.append(StringUtils.lowerCase(subModuleName));
				daoPathBuffer.append(separator);
			}
			daoPathBuffer.append(model.get("ClassName"));
			daoPathBuffer.append("Dao.java");
			filePath = daoPathBuffer.toString();
			writeFile(content, filePath);
			logger.info("Dao: {}", filePath);
		}
		
		logger.info("Generate Success.");
	}
	
	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration(Configuration.VERSION_2_3_21));
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 将内容写入文件
	 * @param content
	 * @param filePath
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileUtil.createFile(filePath)){
				FileWriter fileWriter = new FileWriter(filePath, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(content);
				bufferedWriter.close();
				fileWriter.close();
			}else{
				logger.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
