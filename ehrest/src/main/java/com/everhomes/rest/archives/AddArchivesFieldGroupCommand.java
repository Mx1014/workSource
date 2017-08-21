package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupName: 字段组名称</li>
 * </ul>
 */
public class AddArchivesFieldGroupCommand {

    private String fieldGroupName;

    public AddArchivesFieldGroupCommand() {
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

