package com.everhomes.rest.user;

/**
 * <ul>
 *  <li>fieldName:字段名</li>
 *  <li>fieldType:字段类型 参考{@link com.everhomes.rest.user.FieldType}</li>
 *  <li>fieldContentType:字段内容类型 参考{@link com.everhomes.rest.user.FieldContentType}</li>
 *  <li>fieldValue:字段值</li>
 * </ul>
 */
public class RequestFieldDTO {

	private String fieldName;
	
	private String fieldValue;
	
	private String fieldType;
	
	private String fieldContentType;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldContentType() {
		return fieldContentType;
	}

	public void setFieldContentType(String fieldContentType) {
		this.fieldContentType = fieldContentType;
	}
}
