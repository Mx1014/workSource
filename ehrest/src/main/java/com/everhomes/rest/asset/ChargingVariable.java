//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午2:18:44
 */
public class ChargingVariable {
	private String variableIdentifier;
	private String variableName;
	private String variableValue;
	
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
	public String getVariableValue() {
		return variableValue;
	}
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
