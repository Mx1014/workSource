//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>variableId:变量id</li>
 * <li>variableName:变量名称</li>
 * <li>variableValue:变量值</li>
 *</ul>
 */
public class ChargingItemVariable {
    private Long variableId;

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    private String variableName;
    private BigDecimal variableValue;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public BigDecimal getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(BigDecimal variableValue) {
        this.variableValue = variableValue;
    }

    public ChargingItemVariable() {

    }
}
