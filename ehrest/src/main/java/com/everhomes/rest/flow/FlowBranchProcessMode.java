package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>SINGLE("single"): 唯一分支</li>
 *     <li>CONCURRENT("concurrent"): 并发执行</li>
 * </ul>
 */
public enum FlowBranchProcessMode {

    SINGLE("single"), CONCURRENT("concurrent");

    private String code;

    FlowBranchProcessMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowBranchProcessMode fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (FlowBranchProcessMode t : FlowBranchProcessMode.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }
        return null;
    }
}
