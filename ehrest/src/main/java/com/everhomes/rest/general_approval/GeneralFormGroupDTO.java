package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 表单字段组名</li>
 * <li>fieldGroupDisplayName: 表单字段组显示名</li>
 * <li>fieldGroupAttribute: 字段属性 比如：系统默认字段 {@link GeneralFormFieldAttribute}</li>
 * </ul>
 */
public class GeneralFormGroupDTO {

    private String fieldGroupName;

    private String fieldGroupDisplayName;

    private String fieldGroupAttribute;

    public GeneralFormGroupDTO() {
    }

    public String getFieldGroupName() {
        return fieldGroupName;
    }

    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    public String getFieldGroupDisplayName() {
        return fieldGroupDisplayName;
    }

    public void setFieldGroupDisplayName(String fieldGroupDisplayName) {
        this.fieldGroupDisplayName = fieldGroupDisplayName;
    }

    public String getFieldGroupAttribute() {
        return fieldGroupAttribute;
    }

    public void setFieldGroupAttribute(String fieldGroupAttribute) {
        this.fieldGroupAttribute = fieldGroupAttribute;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
