package ${packageName}.${moduleName}.entity${subModuleName};

import java.util.Date;


/**
 * ${functionName}Entity
 * @author ${classAuthor}
 * @version ${classVersion}
 */
public class ${ClassName}{
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private String name; 	// 名称
    <#list propertyList as pl>
    private ${pl.javaType} ${pl.propertyName};	// ${pl.comment}
    </#list>
	private String remarks;	// 备注
	private String createBy;	// 创建者
	private String createDate;// 创建日期
	private String updateBy;	// 更新者
	private String updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）

	public ${ClassName}() {
		super();
	}

	public ${ClassName}(Long id){
		this();
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	<#list propertyList as pl>
    public ${pl.javaType} get${pl.methodName}() {
        return ${pl.propertyName};
    }

    public void set${pl.methodName}(${pl.javaType} ${pl.propertyName}) {
        this.${pl.propertyName} = ${pl.propertyName};
    }
    
    </#list>
    
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}