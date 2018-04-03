package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>START("start"): 开始</li>
 *     <li>END("end"): 结束</li>
 *     <li>NORMAL("normal"): 普通节点</li>
 *     <li>CONDITION_FRONT("condition_front"): 前置条件</li>
 *     <li>CONDITION_BACK("condition_back"): 后置条件</li>
 * </ul>
 */
public enum FlowNodeType {

    START("start"), END("end"), NORMAL("normal"), CONDITION_FRONT("condition_front"), CONDITION_BACK("condition_back");

    private String code;

    FlowNodeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowNodeType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowNodeType t : FlowNodeType.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }
        return null;
    }
}
