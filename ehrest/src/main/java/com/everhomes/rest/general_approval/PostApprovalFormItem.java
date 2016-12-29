package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldType: 字段类型</li>
 * <li>fieldName: 字段名字</li>
 * <li>fieldValue: 提交的数据</li>
 * <ul>
 * @author janson
 *
 */
public class PostApprovalFormItem {
	private String fieldType;
	private String fieldName;
	private String fieldValue;
	
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
