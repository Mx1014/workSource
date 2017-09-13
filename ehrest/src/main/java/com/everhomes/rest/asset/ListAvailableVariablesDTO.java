//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>variableId:变量id</li>
 * <li>variableName:变量名称</li>
 *</ul>
 */
public class ListAvailableVariablesDTO {
    private Long variableId;
    private String variableName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public ListAvailableVariablesDTO() {

    }
}
