package ${packageName}.${moduleName}.entity${subModuleName};

import java.util.Date;


/**
 * ${functionName}Entity
 * @author ${classAuthor}
 * @version ${classVersion}
 */
public class ${ClassName} extends DataEntity<${ClassName}> {
	
    <#list propertyList as pl>
    private ${pl.javaType} ${pl.propertyName};	// ${pl.comment}
    </#list>

	public ${ClassName}() {
		super();
	}

	public ${ClassName}(Long id){
		this();
		this.id = id;
	}
	
	<#list propertyList as pl>
    public ${pl.javaType} get${pl.methodName}() {
        return ${pl.propertyName};
    }

    public void set${pl.methodName}(${pl.javaType} ${pl.propertyName}) {
        this.${pl.propertyName} = ${pl.propertyName};
    }
    
    </#list>
    
}