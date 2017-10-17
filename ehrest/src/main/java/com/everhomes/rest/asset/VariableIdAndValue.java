//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

public class VariableIdAndValue {
    private Long variableId;
    private BigDecimal variableValue;
    private String varibleIdentifier;

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public BigDecimal getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(BigDecimal variableValue) {
        this.variableValue = variableValue;
    }

    public String getVaribleIdentifier() {
        return varibleIdentifier;
    }

    public void setVaribleIdentifier(String varibleIdentifier) {
        this.varibleIdentifier = varibleIdentifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
