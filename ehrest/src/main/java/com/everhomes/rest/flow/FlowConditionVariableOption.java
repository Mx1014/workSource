package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>displayName: 显示名称</li>
 *     <li>value: 提交值</li>
 * </ul>
 */
public class FlowConditionVariableOption {

    private String displayName;
    private String value;

    public FlowConditionVariableOption(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
