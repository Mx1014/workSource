package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 表单字段组名</li>
 * </ul>
 */
public class GeneralFormGroupDTO {

    private String fieldGroupName;

    public GeneralFormGroupDTO() {
    }

    public String getFieldGroupName() {
        return fieldGroupName;
    }

    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
