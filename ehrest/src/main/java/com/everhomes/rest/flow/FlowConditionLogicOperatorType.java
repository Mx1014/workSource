package com.everhomes.rest.flow;


/**
 * <ul>
 *     <li>OR("||"): OR</li>
 *     <li>AND("&&"): AND</li>
 * </ul>
 */
public enum FlowConditionLogicOperatorType {

    OR("||"),
    AND("&&"),;

    private String code;

    FlowConditionLogicOperatorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowConditionLogicOperatorType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowConditionLogicOperatorType t : FlowConditionLogicOperatorType.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }

        return null;
    }
}
