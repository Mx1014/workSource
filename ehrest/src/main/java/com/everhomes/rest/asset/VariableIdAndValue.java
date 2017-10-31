//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

public class VariableIdAndValue {
//    private Long variableId;
//    private BigDecimal variableValue;
//    private String varibleIdentifier;
//
//    public Long getVariableId() {
//        return variableId;
//    }
//
//    public void setVariableId(Long variableId) {
//        this.variableId = variableId;
//    }
//
//    public BigDecimal getVariableValue() {
//        return variableValue;
//    }
//
//    public void setVariableValue(BigDecimal variableValue) {
//        this.variableValue = variableValue;
//    }
//
//    public String getVaribleIdentifier() {
//        return varibleIdentifier;
//    }
//
//    public void setVaribleIdentifier(String varibleIdentifier) {
//        this.varibleIdentifier = varibleIdentifier;
//    }
//
//    @Override
//    public String toString() {
//        return StringHelper.toJsonString(this);
//    }
//
//    public VariableIdAndValue(Long variableId, BigDecimal variableValue, String varibleIdentifier) {
//        this.variableId = variableId;
//        this.variableValue = variableValue;
//        this.varibleIdentifier = varibleIdentifier;
//    }
//
//    public VariableIdAndValue() {
//    }
    private Object variableId;
    private Object variableValue;
    private Object varibleIdentifier;

    public Object getVariableId() {
        return variableId;
    }

    public void setVariableId(Object variableId) {
        this.variableId = variableId;
    }

    public Object getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(Object variableValue) {
        this.variableValue = variableValue;
    }

    public Object getVaribleIdentifier() {
        return varibleIdentifier;
    }

    public void setVaribleIdentifier(Object varibleIdentifier) {
        this.varibleIdentifier = varibleIdentifier;
    }
}
