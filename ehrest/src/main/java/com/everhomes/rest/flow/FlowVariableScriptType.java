// @formatter:off
package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>BEAN_ID("bean_id"): BEAN_ID</li>
 *     <li>MODULE_ID("module_id"): MODULE_ID</li>
 * </ul>
 */
public enum FlowVariableScriptType {

    BEAN_ID("bean_id"), MODULE_ID("module_id");

    private String code;

    FlowVariableScriptType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowVariableScriptType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowVariableScriptType t : FlowVariableScriptType.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }
        return null;
    }
}
