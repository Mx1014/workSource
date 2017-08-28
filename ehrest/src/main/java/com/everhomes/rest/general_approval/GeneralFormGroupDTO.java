package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 表单字段组名</li>
 * <li>fieldGroupId: 表单字段组id</li>
 * </ul>
 */
public class GeneralFormGroupDTO {

    private String fieldGroupName;

    private Long fieldGroupId;

    public GeneralFormGroupDTO() {
    }

    public String getFieldGroupName() {
        return fieldGroupName;
    }

    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    public Long getFieldGroupId() {
        return fieldGroupId;
    }

    public void setFieldGroupId(Long fieldGroupId) {
        this.fieldGroupId = fieldGroupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
