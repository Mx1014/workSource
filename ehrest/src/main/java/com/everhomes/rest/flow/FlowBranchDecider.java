package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>PROCESSOR("processor"): 处理人指定</li>
 *     <li>CONDITION("condition"): 条件判断</li>
 * </ul>
 */
public enum FlowBranchDecider {

    PROCESSOR("processor"), CONDITION("condition");

    private String code;

    FlowBranchDecider(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowBranchDecider fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowBranchDecider t : FlowBranchDecider.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }
        return null;
    }
}
