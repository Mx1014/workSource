package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>SYSTEM_SCRIPT("system_script"): 通用脚本</li>
 *     <li>USER_SCRIPT("user_script"): 自定义脚本</li>
 * </ul>
 */
public enum FlowScriptCategory {

    SYSTEM_SCRIPT("system_script"),
    USER_SCRIPT("user_script"),;


    private String code;

    FlowScriptCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FlowScriptCategory fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (FlowScriptCategory t : FlowScriptCategory.values()) {
            if (code.equalsIgnoreCase(t.getCode())) {
                return t;
            }
        }
        return null;
    }
}
