package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>fieldName:字段名</li>
 *  <li>fieldDisplayName:字段展示名</li>
 *  <li>fieldType:字段类型 参考{@link com.everhomes.rest.user.FieldType}</li>
 *  <li>fieldContentType:字段内容类型 参考{@link com.everhomes.rest.user.FieldContentType}</li>
 *  <li>fieldDesc:字段描述</li>
 *  <li>requiredFlag: 是否必填 0-否 1-是</li>
 *  <li>dynamicFlag: 是否动态字段 0-否，显示fieldDesc 1-是，显示动态值</li>
 * </ul>
 */
public class FieldDTO {

	private String fieldName;
	
	private String fieldDisplayName;
	
	private String fieldType;
	
	private String fieldContentType;
	
	private String fieldDesc;
	
	private Byte requiredFlag;
	
	private Byte dynamicFlag;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDisplayName() {
		return fieldDisplayName;
	}

	public void setFieldDisplayName(String fieldDisplayName) {
		this.fieldDisplayName = fieldDisplayName;
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

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public Byte getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(Byte requiredFlag) {
		this.requiredFlag = requiredFlag;
	}
	
	public Byte getDynamicFlag() {
		return dynamicFlag;
	}

	public void setDynamicFlag(Byte dynamicFlag) {
		this.dynamicFlag = dynamicFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
