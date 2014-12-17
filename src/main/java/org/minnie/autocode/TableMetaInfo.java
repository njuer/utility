package org.minnie.autocode;

public class TableMetaInfo {

	private String columnName;//字段名称
	private String  fullyQualifiedName;//字段对应的完整Java类名
	private String clazzName;// 字段对应Java类型
	private String propertyName;// 字段对应Java属性名称
	private String remark;//字段备注
	private int columnDisplaySize;//字段长度
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getClazzName() {
		return clazzName;
	}
	
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	@Override
	public String toString() {
		return "TableMetaInfo [columnName=" + columnName
				+ ", fullyQualifiedName=" + fullyQualifiedName + ", clazzName="
				+ clazzName + ", propertyName=" + propertyName + ", remark="
				+ remark + ", columnDisplaySize=" + columnDisplaySize + "]";
	}

}