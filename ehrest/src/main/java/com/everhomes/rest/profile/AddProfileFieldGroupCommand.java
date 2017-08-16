package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 字段组名称</li>
 * </ul>
 */
public class AddProfileFieldGroupCommand {

    private String fieldGroupName;

    public AddProfileFieldGroupCommand() {
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

