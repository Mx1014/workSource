package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 表单字段组名</li>
 * <li>fieldAttribute: 字段属性 比如：系统默认字段 {@link com.everhomes.rest.general_approval.GeneralFormFieldAttributeType}</li>
 * </ul>
 */
public class GeneralFormGroupDTO {

    private String fieldGroupName;

    private String fieldAttribute;

    public GeneralFormGroupDTO() {
    }

    public String getFieldGroupName() {
        return fieldGroupName;
    }

    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    public String getFieldAttribute() {
        return fieldAttribute;
    }

    public void setFieldAttribute(String fieldAttribute) {
        this.fieldAttribute = fieldAttribute;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
