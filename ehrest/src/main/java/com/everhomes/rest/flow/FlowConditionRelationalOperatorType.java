package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>EQUAL("=="): EQUAL</li>
 *     <li>NOT_EQUAL("!="): NOT_EQUAL</li>
 *     <li>GREATER_THEN(">"): GREATER_THEN</li>
 *     <li>GREATER_OR_EQUAL(">="): GREATER_OR_EQUAL</li>
 *     <li>LESS_THEN("<"): LESS_THEN</li>
 *     <li>LESS_OR_EQUAL("<="): LESS_OR_EQUAL</li>
 *     <li>CONTAIN("contain"): CONTAIN</li>
 * </ul>
 */
public enum FlowConditionRelationalOperatorType {

    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THEN(">"),
    GREATER_OR_EQUAL(">="),
    LESS_THEN("<"),
    LESS_OR_EQUAL("<="),
    CONTAIN("contain"),
    NOT_CONTAIN("not_contain"),
    ;

    private String code;

    FlowConditionRelationalOperatorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowConditionRelationalOperatorType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowConditionRelationalOperatorType t : FlowConditionRelationalOperatorType.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }

        return null;
    }
}
