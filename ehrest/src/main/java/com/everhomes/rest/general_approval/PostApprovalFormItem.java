package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldType: 字段类型 {@link GeneralFormFieldType}</li>
 * <li>fieldName: 字段名字</li>
 * <li>fieldDisplayName: 字段展示名字</li>
 * <li>fieldValue: 提交的数据
 * 数字值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 文本值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 日期值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue} 
 * 文本值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 下拉框值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 图片值：{@link com.everhomes.rest.general_approval.PostApprovalFormImageValue}
 * 文件值：{@link com.everhomes.rest.general_approval.PostApprovalFormFileValue}
 * 子表单值：{@link com.everhomes.rest.general_approval.PostApprovalFormSubformValue}
 * </li>
 * <ul>
 * @author janson
 *
 */
public class PostApprovalFormItem {
	private String fieldType;
	private String fieldName;
	private String fieldValue;
	private String fieldDisplayName;

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
