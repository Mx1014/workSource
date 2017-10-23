package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>VARIABLE("variable"): 变量类型，${amount}</li>
 *     <li>CONST("const"): 常量类型，数字常量</li>
 * </ul>
 */
public enum FlowConditionExpressionVarType {

    VARIABLE("variable"), CONST("const");

    private String code;

    FlowConditionExpressionVarType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowConditionExpressionVarType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowConditionExpressionVarType t : FlowConditionExpressionVarType.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }

        return null;
    }
}
