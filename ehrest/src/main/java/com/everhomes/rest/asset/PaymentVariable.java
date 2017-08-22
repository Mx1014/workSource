//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

/**
 *<ul>
 * <li>variableIdentifier:变量标识，有id标识的作用</li>
 * <li>variableName:变量名称</li>
 * <li>variableValue:变量值</li>
 *</ul>
 */
public class PaymentVariable {
    private String variableIdentifier;
    private String variableName;
    private BigDecimal variableValue;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getVariableIdentifier() {
        return variableIdentifier;
    }

    public void setVariableIdentifier(String variableIdentifier) {
        this.variableIdentifier = variableIdentifier;
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

    public PaymentVariable() {

    }
}
